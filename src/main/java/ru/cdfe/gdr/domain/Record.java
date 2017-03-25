package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.core.Relation;
import ru.cdfe.gdr.constants.Relations;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.List;

@Document
@Data
@Relation(collectionRelation = Relations.RECORDS)
public class Record implements Identifiable<String> {
    /**
     * Internal generated id
     */
    @Id
    @JsonIgnore
    private String id;
    
    /**
     * Enables optimistic concurrency control
     */
    @Version
    @JsonIgnore
    private BigInteger version;
    
    @NotEmpty
    @Pattern(regexp = "[A-Z0-9]{8}")
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
}
