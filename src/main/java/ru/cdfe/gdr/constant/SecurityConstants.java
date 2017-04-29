package ru.cdfe.gdr.constant;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public interface SecurityConstants {
    String DEFAULT_USER_NAME = "operator";
    String DEFAULT_USER_SECRET = "operator";
    int AUTH_TOKEN_LENGTH_BYTES = 32;
    String AUTH_HEADER_NAME = "X-Gdr-Token";
    Duration AUTH_SESSION_LENGTH = Duration.of(10, ChronoUnit.MINUTES);
}
