package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoExforDataException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.NO_EXFOR_DATA;
    }
    
    public NoExforDataException() {
    }
    
    public NoExforDataException(String message) {
        super(message);
    }
    
    public NoExforDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoExforDataException(Throwable cause) {
        super(cause);
    }
}
