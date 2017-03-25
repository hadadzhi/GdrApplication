package ru.cdfe.gdr.exceptions.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {
    public UserExistsException() {
    }
    
    public UserExistsException(String message) {
        super(message);
    }
    
    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserExistsException(Throwable cause) {
        super(cause);
    }
}
