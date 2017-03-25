package ru.cdfe.gdr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OptimisticLockingException extends RuntimeException {
    public OptimisticLockingException() {
    }
    
    public OptimisticLockingException(String message) {
        super(message);
    }
    
    public OptimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public OptimisticLockingException(Throwable cause) {
        super(cause);
    }
}
