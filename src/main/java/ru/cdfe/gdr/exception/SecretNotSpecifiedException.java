package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SecretNotSpecifiedException extends GdrException {
@Override
public String getErrorCode(){
	return ErrorCodes.SECRET_NOT_SPECIFIED;
}

public SecretNotSpecifiedException(){
}

public SecretNotSpecifiedException(String message){
	super(message);
}

public SecretNotSpecifiedException(String message, Throwable cause){
	super(message, cause);
}

public SecretNotSpecifiedException(Throwable cause){
	super(cause);
}
}
