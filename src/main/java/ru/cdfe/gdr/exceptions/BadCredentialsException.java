package ru.cdfe.gdr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constants.ErrorCodes;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BadCredentialsException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.BAD_CREDENTIALS;
    }
}
