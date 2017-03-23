package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import ru.cdfe.gdr.validation.Finite;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE) // For Jackson
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
    
    public Quantity(double value, double error, String dimension) {
        this.value = value;
        this.error = error;
        this.dimension = dimension;
    }
}
