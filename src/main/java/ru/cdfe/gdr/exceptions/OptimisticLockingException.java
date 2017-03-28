package ru.cdfe.gdr.exceptions;

import ru.cdfe.gdr.constants.ErrorCodes;

public class OptimisticLockingException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.CONCURRENT_EDITING;
    }
}
