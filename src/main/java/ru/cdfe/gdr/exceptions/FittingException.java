package ru.cdfe.gdr.exceptions;

public class FittingException extends BadRequestException {
    public FittingException() {
    }
    
    public FittingException(String message) {
        super(message);
    }
    
    public FittingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FittingException(Throwable cause) {
        super(cause);
    }
}
