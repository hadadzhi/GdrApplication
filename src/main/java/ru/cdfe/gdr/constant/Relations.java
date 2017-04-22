package ru.cdfe.gdr.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Relations {
    public static final String FITTER = "fitter";
    public static final String EXFOR = "exfor";
    
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    
    public static final String REPOSITORY = "repository";
    
    public static final String RECORD = "record";
    public static final String RECORDS = "records";
    public static final String RECORDS_SEARCH = "searchRecords";
    
    public static final String USERS = "users";
    public static final String AUTHORITIES = "authorities";
    public static final String CURRENT_USER = "currentUser";
}
