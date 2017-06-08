package ru.cdfe.gdr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cdfe.gdr.domain.security.User;

public interface UserRepository extends MongoRepository<User, String> {
User findByName(String name);
}
