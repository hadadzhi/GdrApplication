package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecordNotFound extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.RECORD_NOT_FOUND;
    }
    
    public RecordNotFound() {
    }
    
    public RecordNotFound(String message) {
        super(message);
    }
    
    public RecordNotFound(String message, Throwable cause) {
        super(message, cause);
    }
    
    public RecordNotFound(Throwable cause) {
        super(cause);
    }
}
