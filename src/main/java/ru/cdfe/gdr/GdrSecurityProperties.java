package ru.cdfe.gdr;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import ru.cdfe.gdr.constant.Security;

import javax.validation.constraints.Min;
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
private Duration tokenExpiry = Security.AUTH_SESSION_LENGTH;

public void setTokenExpiry(long minutes){
	Assert.isTrue(minutes > 0, "Value must be a positive number"); // Can't use validation on the method parameter
	tokenExpiry = Duration.of(minutes, ChronoUnit.MINUTES);
}

/**
 * Length of the authentication token in bytes.
 */
@Min(4)
private int tokenLength = Security.AUTH_TOKEN_LENGTH_BYTES;

private String defaultUserName = Security.DEFAULT_USER_NAME;
private String defaultUserSecret = Security.DEFAULT_USER_SECRET;

private boolean persistentAuthentication = false;
}
