package ru.cdfe.gdr.exceptions.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends RuntimeException {
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
