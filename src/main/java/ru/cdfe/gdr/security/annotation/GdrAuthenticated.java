package ru.cdfe.gdr.security.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Inherited
@Documented
@PreAuthorize("authentication instanceof T(ru.cdfe.gdr.security.GdrAuthenticationToken)")
public @interface GdrAuthenticated {
}
