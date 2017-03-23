package ru.cdfe.gdr.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String AUTH_HEADER_NAME = "X-Gdr-Authentication";
    public static final int AUTH_TOKEN_LENGTH_BYTES = 64;
    public static final TemporalAmount AUTH_SESSION_LENGTH = Duration.of(30, ChronoUnit.MINUTES);
}
