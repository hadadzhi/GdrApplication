package ru.cdfe.gdr;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Data
@Component
@ConfigurationProperties("gdr")
@Validated
public class GdrProperties {
    @NotBlank
    @Pattern(regexp = "[a-z-]{3,8}")
    private String curieName;
    
    @NotBlank
    @URL
    private String curieUrlTemplate;
}
