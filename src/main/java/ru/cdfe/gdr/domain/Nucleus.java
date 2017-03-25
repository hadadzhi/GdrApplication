package ru.cdfe.gdr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) // For Jackson
public class Nucleus {
    @JsonProperty(required = true)
    private int charge;
    
    @JsonProperty(required = true)
    private int mass;
    
    public Nucleus(int charge, int mass) {
        this.charge = charge;
        this.mass = mass;
    }
}
