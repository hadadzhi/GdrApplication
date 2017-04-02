package ru.cdfe.gdr.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.cdfe.gdr.domain.security.User;

public class GdrAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public GdrAuthenticationToken(User user, String token) {
        super(user, token, user.getAuthorities());
    }
    
    public String getToken() {
        return String.class.cast(getCredentials());
    }
}
