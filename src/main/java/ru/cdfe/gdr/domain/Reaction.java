package ru.cdfe.gdr.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE) // For Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE) // For Jackson
public class Reaction {
    @NotNull
    @Valid
    private Nucleus target;
    
    @NotNull
    @Valid
    private Nucleus product;
    
    @NotBlank
    private String incident;
    
    @NotBlank
    private String outgoing;
}
