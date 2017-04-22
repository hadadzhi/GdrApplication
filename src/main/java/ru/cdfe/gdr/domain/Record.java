package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;
import org.springframework.stereotype.Component;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.validation.annotation.ExforSubent;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static java.util.stream.Collectors.joining;

@Document
@Data
@Relation(collectionRelation = Relations.RECORDS)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Record implements Identifiable<String> {
    @Id
    @JsonIgnore
    private String id; // Internal generated id
    
    @Version
    @JsonIgnore
    private BigInteger version; // Enables optimistic concurrency control
    
    @NotEmpty
    @ExforSubent
    private String exforNumber;
    
    @Valid
    @NotNull
    private List<DataPoint> sourceData;
    
    @NotEmpty
    @Valid
    private List<Approximation> approximations;
    
    @NotEmpty
    @Valid
    private List<Reaction> reactions;
    
    @NotNull
    @Valid
    private Quantity integratedCrossSection;
    
    @NotNull
    @Valid
    private Quantity firstMoment;
    
    @NotNull
    @Valid
    private Quantity energyCenter;

    // Denormalized properties for fast access, sorting and filtering
    @JsonProperty(access = READ_ONLY)
    private Quantity maxCrossSection;
    
    @JsonProperty(access = READ_ONLY)
    private Quantity energyAtMaxCrossSection;
    
    @JsonProperty(access = READ_ONLY)
    private Quantity fullWidthAtHalfMaximum;

    @JsonProperty(access = READ_ONLY)
    private Double chiSquaredReduced;
    
    @JsonProperty(access = READ_ONLY)
    private String reactionString;
    
    public void denormalize() {
        final Approximation approximation = getApproximations().get(0);
        final Curve curve = approximation.getCurves().get(0);
        
        setEnergyAtMaxCrossSection(curve.getEnergyAtMaxCrossSection());
        setMaxCrossSection(curve.getMaxCrossSection());
        setFullWidthAtHalfMaximum(curve.getFullWidthAtHalfMaximum());
        setChiSquaredReduced(approximation.getChiSquaredReduced());
        
        setReactionString(getReactions().stream().map(Reaction::toString).collect(joining("+")));
    }
    
    @Component
    static class RecordMongoEventListener extends AbstractMongoEventListener<Record> {
        @Override
        public void onBeforeConvert(BeforeConvertEvent<Record> event) {
            event.getSource().denormalize();
        }
    }
}
