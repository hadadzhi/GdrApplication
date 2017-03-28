package ru.cdfe.gdr.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {
    public static final String VALIDATION_FAILURE = "gdr-error-validation-failure";
    public static final String USERNAME_EXISTS = "gdr-error-username-exists";
    public static final String BAD_CREDENTIALS = "gdr-error-bad-credentials";
    public static final String SECRET_NOT_SPECIFIED = "gdr-error-bad-credentials";
    public static final String BAD_EXFOR_DATA = "gdr-error-bad-exfor-data";
    public static final String NO_EXFOR_DATA = "gdr-error-no-exfor-data";
    public static final String INVALID_EXFOR_COLUMN = "gdr-error-invalid-exfor-column";
    public static final String FITTING_FAILURE ="gdr-error-fitting-failure";
    public static final String CONCURRENT_EDITING = "gdr-error-concurrent-editing";
}
