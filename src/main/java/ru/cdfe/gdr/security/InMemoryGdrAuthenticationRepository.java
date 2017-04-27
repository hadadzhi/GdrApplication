package ru.cdfe.gdr.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@ConditionalOnMissingBean(MongoGdrAuthenticationRepository.class)
public class InMemoryGdrAuthenticationRepository implements GdrAuthenticationRepository {
    private final ConcurrentMap<String, GdrAuthenticationToken> map = new ConcurrentHashMap<>();
    
    @Override
    public GdrAuthenticationToken get(String token) {
        return map.get(token);
    }
    
    @Override
    public GdrAuthenticationToken put(GdrAuthenticationToken auth) {
        Assert.notNull(auth, "authentication must not be null");
        return map.put(auth.getToken(), auth);
    }
    
    @Override
    public GdrAuthenticationToken remove(String token) {
        return map.remove(token);
    }
    
    @Override
    public boolean remove(GdrAuthenticationToken auth) {
        return remove(auth.getToken()) != null;
    }
    
    @Override
    public boolean contains(String token) {
        return map.containsKey(token);
    }
}
