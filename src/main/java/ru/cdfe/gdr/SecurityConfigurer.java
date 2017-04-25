package ru.cdfe.gdr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import ru.cdfe.gdr.security.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    
    @Autowired
    public SecurityConfigurer(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception { //@formatter:off
        http
            /*
             * Disable stuff we don't need that's on by default.
             */
            .httpBasic().disable()
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
            .logout().disable()
            /*
             * We want a stateless application, no HttpSession please.
             */
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            /*
             * Put our glorious authentication filter just before the last-resort anonymous authentication filter.
             */
            .addFilterBefore(tokenAuthenticationFilter, AnonymousAuthenticationFilter.class)
            /*
             * Allow all requests: access control is done through global method security.
             */
            .authorizeRequests().anyRequest().permitAll();
    } //@formatter:on
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
