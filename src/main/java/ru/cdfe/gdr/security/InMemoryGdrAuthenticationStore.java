package ru.cdfe.gdr.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
@ConditionalOnMissingBean(MongoGdrAuthenticationStore.class)
public class InMemoryGdrAuthenticationStore implements GdrAuthenticationStore {
private final ConcurrentMap<String, GdrAuthenticationToken> map = new ConcurrentHashMap<>();

@PostConstruct
private void postConstruct(){
	log.info("Using in-memory authentication store");
}

@Override
public GdrAuthenticationToken get(String token){
	return map.get(token);
}

@Override
public GdrAuthenticationToken put(GdrAuthenticationToken auth){
	Assert.notNull(auth, "authentication must not be null");
	return map.put(auth.getToken(), auth);
}

@Override
public GdrAuthenticationToken remove(String token){
	return map.remove(token);
}

@Override
public boolean remove(GdrAuthenticationToken auth){
	return remove(auth.getToken()) != null;
}

@Override
public boolean contains(String token){
	return map.containsKey(token);
}
}
