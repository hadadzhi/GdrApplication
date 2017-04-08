package ru.cdfe.gdr;

import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import ru.cdfe.gdr.constant.ErrorCodes;
import ru.cdfe.gdr.exception.GdrException;

import java.util.Map;

@Component
public class GdrErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        
        errorAttributes.compute("code", (k, v) -> {
            final Throwable ex = getError(requestAttributes);
            if (ex instanceof GdrException) {
                return GdrException.class.cast(ex).getErrorCode();
            } else if (ex instanceof MethodArgumentNotValidException) {
                return ErrorCodes.VALIDATION_FAILURE;
            }
            return null;
        });
        
        return errorAttributes;
    }
}
