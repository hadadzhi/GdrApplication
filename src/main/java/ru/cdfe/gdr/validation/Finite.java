package ru.cdfe.gdr.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
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
