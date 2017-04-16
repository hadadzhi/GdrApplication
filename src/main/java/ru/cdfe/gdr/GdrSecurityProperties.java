package ru.cdfe.gdr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.cdfe.gdr.constant.SecurityConstants;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Data
@Component
@ConfigurationProperties("gdr.security")
@Validated
public class GdrSecurityProperties {
    /**
     * Maximum amount of time (in minutes) without user activity
     * before the authentication token is invalidated.
     */
    private Duration tokenExpiry = SecurityConstants.AUTH_SESSION_LENGTH;
    
    public void setTokenExpiry(long minutes) {
        tokenExpiry = Duration.of(minutes, ChronoUnit.MINUTES);
    }
    
    /**
     * Length of the authentication token in bytes.
     */
    private int tokenLength = SecurityConstants.AUTH_TOKEN_LENGTH_BYTES;
    
    private String defaultUserName = SecurityConstants.DEFAULT_USER_NAME;
    private String defaultUserSecret = SecurityConstants.DEFAULT_USER_SECRET;
}
