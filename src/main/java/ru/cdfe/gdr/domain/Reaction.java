package ru.cdfe.gdr.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
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
    
    @Override
    public String toString() {
        return String.format("(%d-%d(%s,%s)%d-%d)",
                target.getCharge(), target.getMass(), incident, outgoing, product.getCharge(), product.getMass());
    }
}
