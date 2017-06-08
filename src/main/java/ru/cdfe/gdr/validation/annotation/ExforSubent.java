package ru.cdfe.gdr.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ANNOTATION_TYPE, FIELD, PARAMETER, METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
@Pattern(regexp = "[A-Z0-9]{8}")
@Constraint(validatedBy = {})
public @interface ExforSubent {
String message() default "must be an exfor subent number";

Class<?>[] groups() default {};

Class<? extends Payload>[] payload() default {};
}
