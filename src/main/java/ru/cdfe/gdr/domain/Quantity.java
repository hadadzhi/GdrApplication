package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import ru.cdfe.gdr.validation.Finite;

@Data
@AllArgsConstructor
public class Quantity {
    public static final String NO_DIM = "NO-DIM";
    
    @Finite
    @JsonProperty(required = true)
    private double value;
    
    @Finite
    @JsonProperty(required = true)
    private double error;
    
    @NotBlank
    private String dimension;
    
    public Quantity(double value) {
        this(value, 0, NO_DIM);
    }
    
    public Quantity(double value, String dimension) {
        this(value, 0, dimension);
    }
    
    public Quantity(double value, double error) {
        this(value, error, NO_DIM);
    }
}
