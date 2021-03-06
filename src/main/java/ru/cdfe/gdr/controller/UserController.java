package ru.cdfe.gdr.controller;

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
import org.springframework.web.bind.annotation.*;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.exception.OptimisticLockingException;
import ru.cdfe.gdr.exception.UserNameExistsException;
import ru.cdfe.gdr.exception.UserNotFound;
import ru.cdfe.gdr.repository.UserRepository;
import ru.cdfe.gdr.service.LinkService;
import ru.cdfe.gdr.validation.groups.UserCreation;

import javax.validation.groups.Default;

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
                      PasswordEncoder passwordEncoder){

	this.userRepository = userRepository;
	this.entityLinks = entityLinks;
	this.linkService = linkService;
	this.passwordEncoder = passwordEncoder;
}

@GetMapping
@PreAuthorize("permitAll()")
public PagedResources<Resource<User>> get(Pageable pageable, PagedResourcesAssembler<User> assembler){
	log.debug("GET: page: {}", pageable);

	final PagedResources<Resource<User>> users = assembler.toResource(
			userRepository.findAll(pageable),
			user -> new Resource<>(user, entityLinks.linkForSingleResource(user).withSelfRel()));

	linkService.fixPaginatedSelfLink(users, pageable, entityLinks.linkFor(User.class));

	return users;
}

@GetMapping("{id}")
@PreAuthorize("permitAll()")
public Resource<User> get(@PathVariable String id){
	log.debug("GET: user: {}", id);
	final User user = userRepository.findById(id).orElseThrow(UserNotFound::new);
	return new Resource<>(user, entityLinks.linkForSingleResource(user).withSelfRel());
}

@DeleteMapping("{id}")
public void delete(@PathVariable String id){
	if (!userRepository.existsById(id))
		{
		log.debug("DELETE: user not found: {}", id);
		throw new UserNotFound();
		}
	userRepository.deleteById(id);
	log.debug("DELETE: deleted user: {}", id);
}

@PutMapping("{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void put(@PathVariable String id,
                @RequestBody @Validated User user){

	final User existingUser = userRepository.findById(id).orElse(null);

	if (existingUser == null)
		{
		log.debug("PUT: user not found: {}", id);
		throw new UserNotFound();
		}

	user.setId(existingUser.getId());
	user.setVersion(existingUser.getVersion());

	if (user.getSecret() != null)
		{
		user.setSecret(passwordEncoder.encode(user.getSecret()));
		}
	else
		{
		user.setSecret(existingUser.getSecret());
		}

	try
		{
		log.debug("PUT: saving user: {}", user);
		user = userRepository.save(user);
		log.debug("PUT: saved user:  {}", user);
		}
	catch (DuplicateKeyException e)
		{
		log.debug("PUT: duplicate key: ", e);
		throw new UserNameExistsException();
		}
	catch (OptimisticLockingFailureException e)
		{
		log.debug("PUT: optimistic locking failure: ", e);
		throw new OptimisticLockingException();
		}
}

@PostMapping
public ResponseEntity post(@RequestBody @Validated({Default.class, UserCreation.class}) User user){
	try
		{
		log.debug("POST: inserting user: {}", user);

		user.setSecret(passwordEncoder.encode(user.getSecret()));
		user = userRepository.insert(user);

		log.debug("POST: inserted user:  {}", user);
		}
	catch (DuplicateKeyException e)
		{
		log.debug("POST: duplicate key: ", e);
		throw new UserNameExistsException();
		}

	return ResponseEntity.created(entityLinks.linkForSingleResource(user).toUri()).build();
}
}
