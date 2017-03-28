package ru.cdfe.gdr.exceptions;

import ru.cdfe.gdr.constants.ErrorCodes;

public class NoSuchColumnException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.INVALID_EXFOR_COLUMN;
    }
}
