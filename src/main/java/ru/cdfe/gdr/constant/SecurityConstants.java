package ru.cdfe.gdr.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {
    public static final String DEFAULT_USER_NAME = "operator";
    public static final String DEFAULT_USER_SECRET = "operator";
    public static final int AUTH_TOKEN_LENGTH_BYTES = 32;
    public static final String AUTH_HEADER_NAME = "X-Gdr-Token";
    public static final Duration AUTH_SESSION_LENGTH = Duration.of(10, ChronoUnit.MINUTES);
}
