package ru.cdfe.gdr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
@ConditionalOnProperty("gdr.security.persistent-authentication")
public class MongoGdrAuthenticationRepository implements GdrAuthenticationRepository {
    private final MongoTemplate mongo;
    
    @Autowired
    public MongoGdrAuthenticationRepository(MongoTemplate mongo) {
        this.mongo = mongo;
    }
    
    @Override
    public GdrAuthenticationToken get(String token) {
        return mongo.findById(token, GdrAuthenticationToken.class);
    }
    
    @Override
    public GdrAuthenticationToken put(GdrAuthenticationToken auth) {
        Assert.notNull(auth, "authentication must not be null");
        final GdrAuthenticationToken prev = mongo.findById(auth.getToken(), GdrAuthenticationToken.class);
        mongo.save(auth);
        return prev;
    }
    
    @Override
    public GdrAuthenticationToken remove(String token) {
        final GdrAuthenticationToken prev = mongo.findById(token, GdrAuthenticationToken.class);
        mongo.remove(token);
        return prev;
    }
    
    @Override
    public boolean remove(GdrAuthenticationToken auth) {
        final GdrAuthenticationToken prev = mongo.findById(auth.getToken(), GdrAuthenticationToken.class);
        if (prev != null) {
            mongo.remove(prev.getToken());
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean contains(String token) {
        return mongo.exists(query(where("token").is(token)), GdrAuthenticationToken.class);
    }
}
