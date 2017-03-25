package ru.cdfe.gdr.domain.security.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String token;
}
