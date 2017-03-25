package ru.cdfe.gdr.exceptions.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class SecurityException extends RuntimeException {
    public SecurityException() {
    }
    
    public SecurityException(String message) {
        super(message);
    }
    
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SecurityException(Throwable cause) {
        super(cause);
    }
}
