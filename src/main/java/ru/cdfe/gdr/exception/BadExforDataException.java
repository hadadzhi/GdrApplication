package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadExforDataException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.BAD_EXFOR_DATA;
    }
}
