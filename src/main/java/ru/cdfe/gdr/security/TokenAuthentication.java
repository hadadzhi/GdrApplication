package ru.cdfe.gdr.security;

import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

/**
 * Our glorious {@link Authentication} object. It is shiny and immutable,
 * and {@link #isAuthenticated()} if current time is before {@link #getExpiry()}.
 */
public final class TokenAuthentication implements Authentication {
    private static final String NULL_ARG_MSG = "Arguments to the constructor must not be null";
    
    /**
     * The primary key.
     * Equal tokens {@code <=>} equal {@link TokenAuthentication}s.
     * Clashing tokens {@code =>} disaster.
     */
    private final String token;
    
    private final User user;
    private final String remoteAddr;
    private final Instant expiry;
    
    /**
     * Creates an (already authenticated) {@link TokenAuthentication}.
     * All arguments must not be {@code null}.
     */
    public TokenAuthentication(String token, User user, String remoteAddr, Instant expiry) {
        Assert.notNull(token, NULL_ARG_MSG);
        Assert.notNull(user, NULL_ARG_MSG);
        Assert.notNull(remoteAddr, NULL_ARG_MSG);
        Assert.notNull(expiry, NULL_ARG_MSG);
        
        this.user = user;
        this.token = token;
        this.remoteAddr = remoteAddr;
        this.expiry = expiry;
    }
    
    /**
     * Creates a copy of an existing {@link TokenAuthentication}
     * updating only the expiry date.
     * @throws NullPointerException if auth is {@code null}
     */
    public TokenAuthentication(TokenAuthentication auth, Instant expiry) {
        this(auth.getToken(), auth.getPrincipal(), auth.getRemoteAddr(), expiry);
    }
    
    @Override
    public Set<Authority> getAuthorities() {
        return Collections.unmodifiableSet(user.getAuthorities());
    }
    
    /**
     * @see #getToken()
     */
    @Override
    public String getCredentials() {
        return getToken();
    }
    
    public String getToken() {
        return token;
    }
    
    /**
     * @see #getRemoteAddr()
     */
    @Override
    public String getDetails() {
        return getRemoteAddr();
    }
    
    /**
     * @return the IP address of the client that was granted this authentication object
     */
    public String getRemoteAddr() {
        return remoteAddr;
    }
    
    @Override
    public User getPrincipal() {
        return user;
    }
    
    /**
     * @return {@code true} if the token has not expired.
     */
    @Override
    public boolean isAuthenticated() {
        return Instant.now().isBefore(getExpiry());
    }
    
    /**
     * Has no effect on this object.
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }
    
    @Override
    public String getName() {
        return user.getName();
    }
    
    public Instant getExpiry() {
        return expiry;
    }
    
    @Override
    public int hashCode() {
        return getToken().hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TokenAuthentication) &&
                TokenAuthentication.class.cast(obj).getToken().equals(getToken());
    }
}
