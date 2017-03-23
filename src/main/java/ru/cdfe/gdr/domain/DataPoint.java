package ru.cdfe.gdr.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE) // For Jackson
public class DataPoint {
    @NotNull
    @Valid
    private Quantity energy;
    
    @NotNull
    @Valid
    private Quantity crossSection;
    
    public DataPoint(Quantity energy, Quantity crossSection) {
        this.energy = energy;
        this.crossSection = crossSection;
    }
}
