package ru.cdfe.gdr.domain.security;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String token;
}
