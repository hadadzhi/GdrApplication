package ru.cdfe.gdr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.cdfe.gdr.constant.ErrorCodes;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends GdrException {
@Override
public String getErrorCode(){
	return ErrorCodes.USER_NOT_FOUND;
}

public UserNotFound(){
}

public UserNotFound(String message){
	super(message);
}

public UserNotFound(String message, Throwable cause){
	super(message, cause);
}

public UserNotFound(Throwable cause){
	super(cause);
}
}
