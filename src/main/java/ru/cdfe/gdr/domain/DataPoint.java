package ru.cdfe.gdr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class DataPoint {
    @NotNull
    @Valid
    private Quantity energy;
    
    @NotNull
    @Valid
    private Quantity crossSection;
}
