package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FittingException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.FITTING_FAILURE;
    }
    
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
