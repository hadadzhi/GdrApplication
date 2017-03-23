package ru.cdfe.gdr.security;

import lombok.Data;
import org.springframework.security.core.Authentication;

import java.time.Instant;

@Data
public class AuthenticationInfo {
    private final Authentication authentication;
    private final Instant expiry;
}
