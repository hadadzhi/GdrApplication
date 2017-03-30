package ru.cdfe.gdr.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.hateoas.core.Relation;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.Approximation;
import ru.cdfe.gdr.domain.Curve;
import ru.cdfe.gdr.domain.Nucleus;
import ru.cdfe.gdr.domain.Quantity;
import ru.cdfe.gdr.domain.Record;

@Data
@Relation(collectionRelation = Relations.RECORDS_COMPACT)
public class RecordExcerpt {
    @JsonUnwrapped
    @JsonIgnoreProperties({"approximations", "sourceData", "reactions"})
    private final Record record;
    
    private final Quantity maxCrossSection;
    private final Quantity energyAtMaxCrossSection;
    private final Quantity fullWidthAtHalfMaximum;
    
    private final double chiSquaredReduced;
    
    private final Nucleus target;
    
    public RecordExcerpt(Record record) {
        this.record = record;
        this.target = record.getReactions().get(0).getTarget();
        
        final Approximation approximation = record.getApproximations().get(0);
        this.chiSquaredReduced = approximation.getChiSquaredReduced();
        
        final Curve curve = approximation.getCurves().get(0);
        this.maxCrossSection = curve.getMaxCrossSection();
        this.energyAtMaxCrossSection = curve.getEnergyAtMaxCrossSection();
        this.fullWidthAtHalfMaximum = curve.getFullWidthAtHalfMaximum();
    }
}
