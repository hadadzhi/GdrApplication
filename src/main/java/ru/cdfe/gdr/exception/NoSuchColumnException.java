package ru.cdfe.gdr.exception;

import ru.cdfe.gdr.constant.ErrorCodes;

public class NoSuchColumnException extends GdrException {
@Override
public String getErrorCode(){
	return ErrorCodes.INVALID_EXFOR_COLUMN;
}

public NoSuchColumnException(){
}

public NoSuchColumnException(String message){
	super(message);
}

public NoSuchColumnException(String message, Throwable cause){
	super(message, cause);
}

public NoSuchColumnException(Throwable cause){
	super(cause);
}
}
