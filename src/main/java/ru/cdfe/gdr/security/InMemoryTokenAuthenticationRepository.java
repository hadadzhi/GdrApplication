package ru.cdfe.gdr.security;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryTokenAuthenticationRepository implements TokenAuthenticationRepository {
    private final ConcurrentMap<String, TokenAuthentication> map = new ConcurrentHashMap<>();
    
    @Override
    public TokenAuthentication get(String token) {
        Assert.notNull(token, "token must not be null");
        return map.get(token);
    }
    
    @Override
    public TokenAuthentication put(TokenAuthentication auth) {
        Assert.notNull(auth, "authentication must not be null");
        return map.put(auth.getToken(), auth);
    }
    
    @Override
    public TokenAuthentication remove(String token) {
        Assert.notNull(token, "token must not be null");
        return map.remove(token);
    }
    
    @Override
    public boolean remove(TokenAuthentication auth) {
        Assert.notNull(auth, "authentication must not be null");
        return remove(auth.getToken()) != null;
    }
}
