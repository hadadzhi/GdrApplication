package ru.cdfe.gdr.domain.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Document
@Data
@AllArgsConstructor
@Validated
public class User {
    @Id
    @NotEmpty
    private String name;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String secret;

    @NotEmpty
    private Set<String> authorities;
}
