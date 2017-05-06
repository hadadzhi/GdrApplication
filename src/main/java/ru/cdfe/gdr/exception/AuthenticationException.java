package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.BAD_CREDENTIALS;
    }
    
    public AuthenticationException() {
    }
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
