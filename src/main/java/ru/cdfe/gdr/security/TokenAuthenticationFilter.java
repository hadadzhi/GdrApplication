package ru.cdfe.gdr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cdfe.gdr.constants.Constants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * Checks the incoming request headers for an authentication token and,
 * if it is present and valid, puts the {@link Authentication} associated with
 * that token to the {@link SecurityContextHolder}.
 */
@Component
@Configuration
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationInfoRepository authenticationInfoRepository;
    
    @Autowired
    public TokenAuthenticationFilter(AuthenticationInfoRepository authenticationInfoRepository) {
        this.authenticationInfoRepository = authenticationInfoRepository;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                return; // Do nothing if already authenticated through Spring Security
            }
            
            final String token = request.getHeader(Constants.AUTH_HEADER_NAME);
            if (token != null) {
                final AuthenticationInfo authInfo = authenticationInfoRepository.get(token);
                if (authInfo != null) {
                    final Instant now = Instant.now();
                    if (authInfo.getExpiry().isBefore(now)) {
                        authenticationInfoRepository.remove(token);
                    } else {
                        final Authentication auth = authInfo.getAuthentication();
                        final Instant expiry = now.plus(Constants.AUTH_SESSION_LENGTH);
                        authenticationInfoRepository.put(token, new AuthenticationInfo(auth, expiry));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
    
    /**
     * Disable automatic registration of this filter, as it must be
     * properly registered through Spring Security configuration.
     */
    @Bean
    protected FilterRegistrationBean filterRegistrationBean(TokenAuthenticationFilter f) {
        final FilterRegistrationBean frb = new FilterRegistrationBean(f);
        frb.setEnabled(false);
        return frb;
    }
}
