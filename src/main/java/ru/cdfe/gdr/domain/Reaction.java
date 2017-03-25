package ru.cdfe.gdr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
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
