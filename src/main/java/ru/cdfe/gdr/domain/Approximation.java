package ru.cdfe.gdr.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.cdfe.gdr.validation.Finite;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED) // For Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For Jackson
public class Approximation {
    @NotBlank
    private String description;
    
    @Finite
    private double chiSquared;
    
    @Finite
    private double chiSquaredReduced;
    
    @NotNull
    @Valid
    private List<DataPoint> sourceData;

    @NotEmpty
    @Valid
    private List<Curve> curves;
}
