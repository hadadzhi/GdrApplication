package ru.cdfe.gdr.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Relations {
    public static final String FITTER = "fitter";
    
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    
    public static final String REPOSITORY = "repository";
    
    public static final String RECORD = "record";
    public static final String RECORDS = "records";
    public static final String RECORDS_COMPACT = "recordExcerpts";
    public static final String NEW_RECORD = "newRecord";
}
