package ru.cdfe.gdr.domain.security;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class AuthenticationRequest {
@NotEmpty
private final String name;

@NotEmpty
private final String secret;
}
