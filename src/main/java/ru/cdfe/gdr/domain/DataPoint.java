package ru.cdfe.gdr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {
    @NotNull
    @Valid
    private Quantity energy;
    
    @NotNull
    @Valid
    private Quantity crossSection;
}
