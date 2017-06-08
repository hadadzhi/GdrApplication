package ru.cdfe.gdr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.cdfe.gdr.constant.CommandLineOptions;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.repository.UserRepository;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * When there are no users in the database, or when explicitly instructed to
 * by the {@link CommandLineOptions#CREATE_DEFAULT_USER command line switch},
 * creates a default user with all {@link Authority authorities},
 * allowed to log in only from the same machine that the server is running on.
 */
@Slf4j
@Component
class DefaultUserCreator implements ApplicationRunner {
private final UserRepository userRepository;
private final GdrSecurityProperties securityProperties;
private final PasswordEncoder passwordEncoder;

@Autowired
public DefaultUserCreator(UserRepository userRepository,
                          GdrSecurityProperties securityProperties,
                          PasswordEncoder passwordEncoder) {
  
  this.userRepository = userRepository;
  this.securityProperties = securityProperties;
  this.passwordEncoder = passwordEncoder;
}

@Override
public void run(ApplicationArguments args) throws Exception {
  if (userRepository.count() == 0 || args.containsOption(CommandLineOptions.CREATE_DEFAULT_USER)) {
    User defaultUser = new User();
    
    defaultUser.setName(securityProperties.getDefaultUserName());
    defaultUser.setSecret(passwordEncoder.encode(securityProperties.getDefaultUserSecret()));
    defaultUser.setAuthorities(Arrays.stream(Authority.values()).collect(toSet()));
    defaultUser.setAllowedAddresses(Stream.of("::1", "127.0.0.1").collect(toSet()));
    
    final User existingUser = userRepository.findByName(defaultUser.getName());
    if (existingUser != null) {
      log.warn("Default user already exists, overwriting");
      
      defaultUser.setId(existingUser.getId());
      defaultUser.setVersion(existingUser.getVersion());
      
      defaultUser = userRepository.save(defaultUser);
    } else {
      defaultUser = userRepository.insert(defaultUser);
    }
    
    log.info("Created default user: {}, with password: {}",
        defaultUser, securityProperties.getDefaultUserSecret());
  }
}
}
