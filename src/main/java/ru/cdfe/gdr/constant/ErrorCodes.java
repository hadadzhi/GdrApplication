package ru.cdfe.gdr.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodes {
    //@formatter:off
    public static final String VALIDATION_FAILURE       = "gdr-error-validation-failure";
    public static final String USERNAME_EXISTS          = "gdr-error-username-exists";
    public static final String AUTHENTICATION           = "gdr-error-authentication";
    public static final String SECRET_NOT_SPECIFIED     = "gdr-error-password-not-specified";
    public static final String BAD_EXFOR_DATA           = "gdr-error-bad-exfor-data";
    public static final String NO_EXFOR_DATA            = "gdr-error-no-exfor-data";
    public static final String INVALID_EXFOR_COLUMN     = "gdr-error-invalid-exfor-column";
    public static final String FITTING_FAILURE          = "gdr-error-fitting-failure";
    public static final String CONCURRENT_EDITING       = "gdr-error-concurrent-editing";
    public static final String RECORD_NOT_FOUND         = "gdr-error-record-not-found";
    public static final String USER_NOT_FOUND           = "gdr-error-user-not-found";
    //@formatter:on
}
