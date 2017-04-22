package ru.cdfe.gdr.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Data
public final class SearchRequest {
    @NotNull
    private final Map<String, Object> where = Collections.emptyMap();
    
    @NotNull
    private final Set<String> select = Collections.emptySet();
}
