package ru.cdfe.gdr;


import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import ru.cdfe.gdr.exceptions.GdrException;

import java.util.Map;

@Component
public class GdrErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        addErrorCode(errorAttributes, getError(requestAttributes));
        return errorAttributes;
    }
    
    private void addErrorCode(Map<String, Object> attributes, Throwable exception) {
        if (exception instanceof GdrException) {
            attributes.put("code", GdrException.class.cast(exception).getErrorCode());
        } else if (exception instanceof MethodArgumentNotValidException) {
            attributes.put("code", "gdr-validation-error");
        }
    }
}
