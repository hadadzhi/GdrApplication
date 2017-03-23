package ru.cdfe.gdr;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@ConfigurationProperties("gdr")
@Validated
public class GdrApplicationProperties {
    @NotBlank
    private String curieName;
    
    @NotBlank
    private String curieUrlTemplate;
}
