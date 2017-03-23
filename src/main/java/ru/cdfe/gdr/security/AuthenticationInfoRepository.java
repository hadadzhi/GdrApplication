package ru.cdfe.gdr.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Maps {@link String} tokens to {@link AuthenticationInfo} instances
 * representing the user identified by the token.
 */
@Component
public class AuthenticationInfoRepository {
    private final ConcurrentMap<String, AuthenticationInfo> map = new ConcurrentHashMap<>();
    
    /**
     * Returns the authentication for the specified token or {@code null}
     * if there is no authentication for this token.
     */
    public AuthenticationInfo get(String token) {
        return map.get(token);
    }
    
    /**
     * Associates the specified authentication with the specified token.
     * @return the previous authentication associated with the token, or {@code null}
     * if there was no authentication for the specified token.
     */
    public AuthenticationInfo put(String token, AuthenticationInfo authInfo) {
        return map.put(token, authInfo);
    }
    
    /**
     * Removes the authentication for the specified token.
     * @return the previous authentication associated with the token, or {@code null}
     * if there was no authentication for the specified token.
     */
    public AuthenticationInfo remove(String token) {
        return map.remove(token);
    }
}
