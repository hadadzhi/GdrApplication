package ru.cdfe.gdr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cdfe.gdr.GdrSecurityProperties;
import ru.cdfe.gdr.constant.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * Checks the incoming request headers for an authentication token and,
 * if it is present and valid, puts the {@link TokenAuthentication} associated with
 * that token to the {@link SecurityContextHolder}.
 */
@Component
@Configuration
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenAuthenticationRepository tokenAuthenticationRepository;
    private final GdrSecurityProperties securityProperties;
    
    @Autowired
    public TokenAuthenticationFilter(TokenAuthenticationRepository tokenAuthenticationRepository,
                                     GdrSecurityProperties securityProperties) {
        
        this.tokenAuthenticationRepository = tokenAuthenticationRepository;
        this.securityProperties = securityProperties;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
            if (token != null) {
                final TokenAuthentication auth = tokenAuthenticationRepository.get(token);
                if (auth != null) {
                    if (!auth.isAuthenticated()) {
                        tokenAuthenticationRepository.remove(token);
                    } else {
                        if (auth.getRemoteAddr().equals(request.getRemoteAddr())) {
                            tokenAuthenticationRepository.put(new TokenAuthentication(auth,
                                    Instant.now().plus(securityProperties.getTokenExpiry())));
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Disable automatic registration of this filter, as it must be
     * properly registered through Spring Security configuration.
     */
    @Bean
    protected FilterRegistrationBean<TokenAuthenticationFilter>
    filterRegistrationBean(TokenAuthenticationFilter f) {
        final FilterRegistrationBean<TokenAuthenticationFilter> frb = new FilterRegistrationBean<>(f);
        frb.setEnabled(false);
        return frb;
    }
}
