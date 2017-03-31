package ru.cdfe.gdr.security;

/**
 * Maps {@link String} tokens to {@link AuthenticationInfo} instances
 * representing the user identified by the token.
 */
public interface AuthenticationInfoRepository {
    /**
     * Returns the authentication for the specified token or {@code null}
     * if there is no authentication for this token.
     */
    AuthenticationInfo get(String token);
    
    /**
     * Associates the specified authentication with the specified token.
     * @return the previous authentication associated with the token, or {@code null}
     * if there was no authentication for the specified token.
     */
    AuthenticationInfo put(String token, AuthenticationInfo authInfo);
    
    /**
     * Removes the authentication for the specified token.
     * @return the previous authentication associated with the token, or {@code null}
     * if there was no authentication for the specified token.
     */
    AuthenticationInfo remove(String token);
}
