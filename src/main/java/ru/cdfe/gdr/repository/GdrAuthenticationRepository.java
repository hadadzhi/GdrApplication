package ru.cdfe.gdr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cdfe.gdr.security.GdrAuthenticationToken;

public interface GdrAuthenticationRepository extends MongoRepository<GdrAuthenticationToken, String> {
}
