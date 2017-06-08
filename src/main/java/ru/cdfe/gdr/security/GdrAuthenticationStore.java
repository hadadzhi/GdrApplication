package ru.cdfe.gdr.security;

/**
 * Maps {@link String} tokens to {@link GdrAuthenticationToken} instances
 * representing the user identified by the token.
 */
public interface GdrAuthenticationStore {
/**
 * Returns the authentication for the specified token or {@code null}
 * if there is no authentication for this token.
 */
GdrAuthenticationToken get(String token);

/**
 * Adds the specified authentication to the repository, replacing any existing one.
 * @return the previous authentication for the same token, or {@code null}
 * if there was no such authentication
 * @throws IllegalArgumentException if {@code auth} is {@code null}
 */
GdrAuthenticationToken put(GdrAuthenticationToken auth);

/**
 * Removes the authentication for the specified token.
 * @return the previous authentication associated with the token, or {@code null}
 * if there was no authentication for the specified token.
 */
GdrAuthenticationToken remove(String token);

/**
 * Removes the specified authentication.
 * @return {@code true} if the repository's content was changed as a result of this call
 */
boolean remove(GdrAuthenticationToken auth);

/**
 * Checks if the repository contains a {@link GdrAuthenticationToken}
 * for the specified token.
 */
boolean contains(String token);
}
