package ru.cdfe.gdr.security;

/**
 * Maps {@link String} tokens to {@link TokenAuthentication} instances
 * representing the user identified by the token.
 */
public interface TokenAuthenticationRepository {
    /**
     * Returns the authentication for the specified token or {@code null}
     * if there is no authentication for this token.
     */
    TokenAuthentication get(String token);
    
    /**
     * Adds the specified authentication to the repository, replacing any existing one.
     * @return the previous authentication for the same token, or {@code null}
     * if there was no such authentication
     * @throws IllegalArgumentException if {@code auth} is {@code null}
     */
    TokenAuthentication put(TokenAuthentication auth);
    
    /**
     * Removes the authentication for the specified token.
     * @return the previous authentication associated with the token, or {@code null}
     * if there was no authentication for the specified token.
     */
    TokenAuthentication remove(String token);
    
    /**
     * Removes the specified authentication.
     * @return {@code true} if the repository's content was changed as a result of this call
     */
    boolean remove(TokenAuthentication auth);
    
    /**
     * Checks if the repository contains a {@link TokenAuthentication}
     * for the specified token.
     */
    boolean contains(String token);
}
