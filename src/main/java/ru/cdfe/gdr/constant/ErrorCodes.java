package ru.cdfe.gdr.constant;

public interface ErrorCodes {
//@formatter:off
    String VALIDATION_FAILURE = "gdr-error-validation-failure";
    String USERNAME_EXISTS = "gdr-error-username-exists";
    String BAD_CREDENTIALS = "gdr-error-bad-credentials";
    String SECRET_NOT_SPECIFIED = "gdr-error-password-not-specified";
    String BAD_EXFOR_DATA = "gdr-error-bad-exfor-data";
    String NO_EXFOR_DATA = "gdr-error-no-exfor-data";
    String INVALID_EXFOR_COLUMN = "gdr-error-invalid-exfor-column";
    String FITTING_FAILURE = "gdr-error-fitting-failure";
    String CONCURRENT_EDITING = "gdr-error-concurrent-editing";
    String RECORD_NOT_FOUND = "gdr-error-record-not-found";
    String USER_NOT_FOUND = "gdr-error-user-not-found";
    //@formatter:on
}
