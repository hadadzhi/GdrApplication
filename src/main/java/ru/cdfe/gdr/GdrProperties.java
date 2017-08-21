package ru.cdfe.gdr;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
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
private String curieUriTemplate;

@Min(1)
private int maxPageSize;

@Min(1)
private int defaultPageSize;
}
