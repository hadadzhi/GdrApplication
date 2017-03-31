package ru.cdfe.gdr.security;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryAuthenticationInfoRepository implements AuthenticationInfoRepository {
    private final ConcurrentMap<String, AuthenticationInfo> map = new ConcurrentHashMap<>();
    
    @Override
    public AuthenticationInfo get(String token) {
        Assert.notNull(token, "token may not be null");
        return map.get(token);
    }
    
    @Override
    public AuthenticationInfo put(String token, AuthenticationInfo authInfo) {
        Assert.notNull(token, "token may not be null");
        Assert.notNull(authInfo, "authentication info may not be null");
        return map.put(token, authInfo);
    }
    
    @Override
    public AuthenticationInfo remove(String token) {
        Assert.notNull(token, "token may not be null");
        return map.remove(token);
    }
}
