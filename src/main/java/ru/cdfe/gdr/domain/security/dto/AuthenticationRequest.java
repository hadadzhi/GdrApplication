package ru.cdfe.gdr.domain.security.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class AuthenticationRequest {
    @NotEmpty
    private final String name;
    
    @NotEmpty
    private final String secret;
}
