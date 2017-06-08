package ru.cdfe.gdr.security;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

/**
 * Our glorious {@link Authentication} object. It is shiny and immutable,
 * and {@link #isAuthenticated()} if current time is before {@link #getExpiring()}.
 */
@Document
@ToString
public final class GdrAuthenticationToken implements Authentication {
/**
 * The primary key.
 * Equal tokens {@code <=>} equal {@link GdrAuthenticationToken}s.
 * Clashing tokens {@code =>} disaster.
 */
@Id
private final String token;

@DBRef
private final User user;

private final String remoteAddr;

private final Instant expiring;

private final Instant created;

private final Instant updated;

/**
 * Creates an (already authenticated) {@link GdrAuthenticationToken}.
 * All arguments must not be {@code null}.
 */
@PersistenceConstructor
public GdrAuthenticationToken(String token, User user, String remoteAddr, Instant expiring) {
  assertAllNotNull(token, user, remoteAddr, expiring);
  this.created = Instant.now();
  this.updated = this.created;
  this.token = token;
  this.user = user;
  this.remoteAddr = remoteAddr;
  this.expiring = expiring;
}

/**
 * Creates a copy of an existing {@link GdrAuthenticationToken}
 * updating only the expiry date.
 * @throws NullPointerException if auth is {@code null}
 */
public GdrAuthenticationToken(GdrAuthenticationToken auth, Instant expiring) {
  assertAllNotNull(auth, expiring);
  this.updated = Instant.now();
  this.created = auth.created;
  this.token = auth.token;
  this.user = auth.user;
  this.remoteAddr = auth.remoteAddr;
  this.expiring = expiring;
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
  return Instant.now().isBefore(getExpiring());
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

public Instant getExpiring() {
  return expiring;
}

public Instant getCreated() {
  return created;
}

public Instant getUpdated() {
  return updated;
}

@Override
public int hashCode() {
  return getToken().hashCode();
}

@Override
public boolean equals(Object obj) {
  return (obj instanceof GdrAuthenticationToken) &&
      GdrAuthenticationToken.class.cast(obj).getToken().equals(getToken());
}

private static void assertAllNotNull(Object... args) {
  for (final Object arg : args) {
    Assert.notNull(arg, "Arguments must not be null");
  }
}
}
