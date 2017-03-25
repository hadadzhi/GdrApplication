package ru.cdfe.gdr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchRecordException extends RuntimeException {
    public NoSuchRecordException() {
    }
    
    public NoSuchRecordException(String message) {
        super(message);
    }
    
    public NoSuchRecordException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoSuchRecordException(Throwable cause) {
        super(cause);
    }
}
