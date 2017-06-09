package ru.cdfe.gdr.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.cdfe.gdr.GdrSecurityProperties;
import ru.cdfe.gdr.constant.Security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * Checks the incoming request headers for an authentication token and,
 * if it is present and valid, puts the {@link GdrAuthenticationToken} associated with
 * that token to the {@link SecurityContextHolder}.
 */
@Slf4j
@Component
@Configuration
public class GdrAuthenticationFilter extends OncePerRequestFilter {
private final GdrAuthenticationStore gdrAuthenticationStore;
private final GdrSecurityProperties securityProperties;

@Autowired
public GdrAuthenticationFilter(GdrAuthenticationStore gdrAuthenticationStore,
                               GdrSecurityProperties securityProperties) {
  
  this.gdrAuthenticationStore = gdrAuthenticationStore;
  this.securityProperties = securityProperties;
}

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
  
  log.debug("Filtering: {}", request.getMethod() + " " + request.getRequestURI() +
      " from " + request.getRemoteHost() + "(" + request.getRemoteAddr() + ")");
  
  if (SecurityContextHolder.getContext().getAuthentication() == null) {
    final String token = request.getHeader(Security.AUTH_HEADER_NAME);
    if (token != null) {
      final GdrAuthenticationToken auth = gdrAuthenticationStore.get(token);
      if (auth != null) {
        if (!auth.isAuthenticated()) {
          gdrAuthenticationStore.remove(token);
          log.debug("Removed expired authentication: {}", auth);
        } else {
          if (auth.getRemoteAddr().equals(request.getRemoteAddr())) {
            final Instant expiry = Instant.now().plus(securityProperties.getTokenExpiry());
            final GdrAuthenticationToken updatedAuth = new GdrAuthenticationToken(auth, expiry);
            gdrAuthenticationStore.put(updatedAuth);
            SecurityContextHolder.getContext().setAuthentication(updatedAuth);
            log.debug("Updated authentication token: {}", updatedAuth);
          } else log.debug("Request address {} doesn't match the authentication: {}", request, auth);
        }
      } else log.debug("No authentication token for {}", token);
    } else log.debug("No authentication header");
  } else log.debug("Already authenticated: {}", SecurityContextHolder.getContext().getAuthentication());
  
  filterChain.doFilter(request, response);
}

/**
 * Disable automatic registration of this filter, as it must be
 * properly registered through Spring Security configuration.
 */
@Bean
protected FilterRegistrationBean filterRegistrationBean(GdrAuthenticationFilter f) {
  final FilterRegistrationBean frb = new FilterRegistrationBean<>(f);
  frb.setEnabled(false);
  return frb;
}
}
