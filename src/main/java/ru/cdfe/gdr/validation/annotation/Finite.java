package ru.cdfe.gdr.validation.annotation;

import ru.cdfe.gdr.validation.FiniteDoubleValidator;
import ru.cdfe.gdr.validation.FiniteFloatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element's value must not be infinite or NaN.
 * {@code null} elements are considered valid.
 * Accepts {@link Float} and {@link Double}.
 */
@Documented
@Target({ANNOTATION_TYPE, FIELD, PARAMETER, METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
@Constraint(validatedBy = {FiniteDoubleValidator.class, FiniteFloatValidator.class})
public @interface Finite {
    String message() default "may not be infinite or NaN";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
