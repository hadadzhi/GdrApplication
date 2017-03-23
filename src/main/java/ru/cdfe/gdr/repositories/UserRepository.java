package ru.cdfe.gdr.repositories;

import ru.cdfe.gdr.domain.security.User;

public interface UserRepository extends SecuredMongoRepository<User, String> {}
