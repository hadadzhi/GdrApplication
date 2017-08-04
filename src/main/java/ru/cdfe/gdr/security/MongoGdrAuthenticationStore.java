package ru.cdfe.gdr.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.cdfe.gdr.repository.GdrAuthenticationRepository;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@ConditionalOnProperty("gdr.security.persistent-authentication")
public class MongoGdrAuthenticationStore implements GdrAuthenticationStore {
    private final GdrAuthenticationRepository repo;
    
    @PostConstruct
    private void postConstruct() {
        log.info("Using persistent authentication store");
    }
    
    @Autowired
    public MongoGdrAuthenticationStore(GdrAuthenticationRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public GdrAuthenticationToken get(String token) {
        return repo.findById(token).orElse(null);
    }
    
    @Override
    public GdrAuthenticationToken put(GdrAuthenticationToken auth) {
        Assert.notNull(auth, "authentication must not be null");
        final GdrAuthenticationToken prev = get(auth.getToken());
        repo.save(auth);
        return prev;
    }
    
    @Override
    public GdrAuthenticationToken remove(String token) {
        final GdrAuthenticationToken prev = get(token);
        repo.deleteById(token);
        return prev;
    }
    
    @Override
    public boolean remove(GdrAuthenticationToken auth) {
        return remove(auth.getToken()) != null;
    }
    
    @Override
    public boolean contains(String token) {
        return repo.existsById(token);
    }
}
