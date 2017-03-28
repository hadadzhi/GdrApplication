package ru.cdfe.gdr.exception;

import ru.cdfe.gdr.constant.ErrorCodes;

public class NoSuchColumnException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.INVALID_EXFOR_COLUMN;
    }
}
