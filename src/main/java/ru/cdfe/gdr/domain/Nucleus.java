package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Nucleus {
    @JsonProperty(required = true)
    private int charge;
    
    @JsonProperty(required = true)
    private int mass;
}
