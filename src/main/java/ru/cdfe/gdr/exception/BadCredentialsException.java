package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.BAD_CREDENTIALS;
    }
    
    public BadCredentialsException() {
    }
    
    public BadCredentialsException(String message) {
        super(message);
    }
    
    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public BadCredentialsException(Throwable cause) {
        super(cause);
    }
}
