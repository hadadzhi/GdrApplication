package ru.cdfe.gdr.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.cdfe.gdr.validation.annotation.Finite;

import javax.validation.Valid;
import java.util.List;

@Data
public class Approximation {
@NotBlank
private String description;

@Finite
private double chiSquared;

@Finite
private double chiSquaredReduced;

@NotEmpty
@Valid
private List<DataPoint> sourceData;

@NotEmpty
@Valid
private List<Curve> curves;
}
