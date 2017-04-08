package ru.cdfe.gdr.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.springframework.hateoas.core.Relation;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.Record;

@Data
@Relation(collectionRelation = Relations.RECORDS_COMPACT)
public class RecordExcerpt {
    @JsonUnwrapped
    @JsonIgnoreProperties({"approximations", "sourceData", "reactions"})
    private final Record record;
}
