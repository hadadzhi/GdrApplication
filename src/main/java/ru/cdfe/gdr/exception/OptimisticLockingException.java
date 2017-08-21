package ru.cdfe.gdr.exception;

import ru.cdfe.gdr.constant.ErrorCodes;

public class OptimisticLockingException extends GdrException {
@Override
public String getErrorCode(){
	return ErrorCodes.CONCURRENT_EDITING;
}

public OptimisticLockingException(){
}

public OptimisticLockingException(String message){
	super(message);
}

public OptimisticLockingException(String message, Throwable cause){
	super(message, cause);
}

public OptimisticLockingException(Throwable cause){
	super(cause);
}
}
