package ru.cdfe.gdr.exception;

public abstract class GdrException extends RuntimeException {
    /**
     * A well-known constant by which the client may identify the error
     */
    public abstract String getErrorCode();
    
    public GdrException() {
    }
    
    public GdrException(String message) {
        super(message);
    }
    
    public GdrException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public GdrException(Throwable cause) {
        super(cause);
    }
}
