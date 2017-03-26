package ru.cdfe.gdr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.exceptions.BadRequestException;
import ru.cdfe.gdr.exceptions.ConflictException;
import ru.cdfe.gdr.exceptions.NotFoundException;
import ru.cdfe.gdr.repositories.UserRepository;
import ru.cdfe.gdr.services.LinkService;

import java.util.Optional;

@Slf4j
@RestController
@ExposesResourceFor(User.class)
@RequestMapping(Relations.REPOSITORY + "/" + Relations.USERS)
@PreAuthorize("hasAuthority(T(ru.cdfe.gdr.domain.security.Authority).USERS)")
public class UserController {
    private final UserRepository userRepository;
    private final EntityLinks entityLinks;
    private final LinkService linkService;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserController(UserRepository userRepository,
                          EntityLinks entityLinks,
                          LinkService linkService,
                          PasswordEncoder passwordEncoder) {
        
        this.userRepository = userRepository;
        this.entityLinks = entityLinks;
        this.linkService = linkService;
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping
    public PagedResources<Resource<User>> getAll(Pageable pageable, PagedResourcesAssembler<User> assembler) {
        log.debug("GET: getting all users");
        final PagedResources<Resource<User>> users = assembler.toResource(
                userRepository.findAll(pageable),
                user -> new Resource<>(user, entityLinks.linkForSingleResource(user).withSelfRel()));
        linkService.fixSelfLink(users, pageable, User.class);
        return users;
    }
    
    @GetMapping("{id}")
    public Resource<User> get(@PathVariable String id) {
        log.debug("GET: getting user: {}", id);
        final User user = Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(NotFoundException::new);
        return new Resource<>(user, entityLinks.linkForSingleResource(user).withSelfRel());
    }
    
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException();
        }
        log.debug("DELETE: deleting user: {}", id);
        userRepository.delete(id);
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@PathVariable String id, @RequestBody @Validated User user) {
        final User existingUser = userRepository.findOne(id);
        
        if (existingUser == null) {
            log.debug("PUT: user {} does not exist", id);
            throw new NotFoundException();
        }
        
        user.setId(existingUser.getId());
        user.setVersion(existingUser.getVersion());
        
        if (user.getSecret() != null) {
            user.setSecret(passwordEncoder.encode(user.getSecret()));
        } else {
            user.setSecret(existingUser.getSecret());
        }
        
        try {
            log.debug("PUT: saving user: {}", user);
            userRepository.save(user);
        } catch (OptimisticLockingFailureException e) {
            log.warn("PUT: failed to save user: {}", e.getMessage());
            throw new ConflictException("Concurrent modification", e);
        } catch (DuplicateKeyException e) {
            log.warn("PUT: failed to save user: {}", e.getMessage());
            throw new ConflictException("User name already exists", e);
        }
    }
    
    @PostMapping
    public ResponseEntity post(@RequestBody @Validated User user) {
        if (user.getSecret() == null) {
            log.debug("POST: password not specified: {}", user);
            throw new BadRequestException("The password must be specified");
        }
        
        user.setSecret(passwordEncoder.encode(user.getSecret()));
    
        try {
            log.debug("POST: inserting user: {}", user);
            user = userRepository.insert(user);
        } catch (DuplicateKeyException e) {
            log.warn("POST: failed to insert user: {}", e.getMessage());
            throw new ConflictException("User name already exists", e);
        }
        
        return ResponseEntity.created(entityLinks.linkForSingleResource(user).toUri()).build();
    }
}
