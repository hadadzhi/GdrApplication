package ru.cdfe.gdr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constants.ErrorCodes;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameExistsException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.USERNAME_EXISTS;
    }
}
