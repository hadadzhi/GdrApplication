package ru.cdfe.gdr.exception;

import ru.cdfe.gdr.constant.ErrorCodes;

public class OptimisticLockingException extends GdrException {
    @Override
    public String getErrorCode() {
        return ErrorCodes.CONCURRENT_EDITING;
    }
}
