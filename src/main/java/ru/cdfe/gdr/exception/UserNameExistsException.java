package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameExistsException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.USERNAME_EXISTS;
    }
    
    public UserNameExistsException() {
        this("A user with the same name already exists");
    }
    
    public UserNameExistsException(String message) {
        super(message);
    }
    
    public UserNameExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserNameExistsException(Throwable cause) {
        super(cause);
    }
}
