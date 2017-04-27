package ru.cdfe.gdr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

interface GdrAuthenticationRepositoryBean extends MongoRepository<GdrAuthenticationToken, String> {}

@Component
@ConditionalOnProperty("gdr.security.persistent-authentication")
public class MongoGdrAuthenticationRepository implements GdrAuthenticationRepository {
    private final GdrAuthenticationRepositoryBean repo;
    
    @Autowired
    public MongoGdrAuthenticationRepository(GdrAuthenticationRepositoryBean repo) {
        this.repo = repo;
    }
    
    @Override
    public GdrAuthenticationToken get(String token) {
        return repo.findOne(token).orElse(null);
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
        repo.delete(token);
        return prev;
    }
    
    @Override
    public boolean remove(GdrAuthenticationToken auth) {
        return remove(auth.getToken()) != null;
    }
    
    @Override
    public boolean contains(String token) {
        return repo.exists(token);
    }
}
