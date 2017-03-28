package ru.cdfe.gdr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constant.SecurityConstants;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.domain.security.dto.AuthenticationRequest;
import ru.cdfe.gdr.domain.security.dto.AuthenticationResponse;
import ru.cdfe.gdr.exception.BadCredentialsException;
import ru.cdfe.gdr.exception.UserNameExistsException;
import ru.cdfe.gdr.exception.OptimisticLockingException;
import ru.cdfe.gdr.repository.UserRepository;
import ru.cdfe.gdr.security.AuthenticationInfo;
import ru.cdfe.gdr.security.AuthenticationInfoRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping
public class AuthenticationController {
    private final AuthenticationInfoRepository authenticationInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    public AuthenticationController(AuthenticationInfoRepository authenticationInfoRepository,
                                    PasswordEncoder passwordEncoder,
                                    UserRepository userRepository) {
        
        this.authenticationInfoRepository = authenticationInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    
    @PostMapping(Relations.LOGIN)
    public AuthenticationResponse login(@RequestBody @Validated AuthenticationRequest authRequest,
                                        HttpServletRequest httpRequest) {
        
        final User user = userRepository.findByName(authRequest.getName());
        
        if (user != null && passwordEncoder.matches(authRequest.getSecret(), user.getSecret())) {
            return loginInternal(user, httpRequest);
        }
        
        log.warn("Login failure: {}", authRequest.getName());
        throw new BadCredentialsException();
    }
    
    private AuthenticationResponse loginInternal(User user, HttpServletRequest httpRequest) {
        final Instant expiry = Instant.now().plus(SecurityConstants.AUTH_SESSION_LENGTH);
        final String token = generateToken();
        
        final UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        
        auth.setDetails(new WebAuthenticationDetails(httpRequest));
        
        authenticationInfoRepository.put(token, new AuthenticationInfo(auth, expiry));
        
        log.info("Login success: {}", user);
        return new AuthenticationResponse(token);
    }
    
    @PostMapping(Relations.LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isFullyAuthenticated()")
    public void logout(Authentication auth) {
        authenticationInfoRepository.remove(String.class.cast(auth.getCredentials()));
        log.info("Logout success: {}", auth.getPrincipal());
    }
    
    @GetMapping(Relations.CURRENT_USER)
    @PreAuthorize("isFullyAuthenticated()")
    public Resource<User> currentUser(@AuthenticationPrincipal User user) {
        return new Resource<>(user, linkTo(AuthenticationController.class)
                .slash(Relations.CURRENT_USER).withSelfRel());
    }
    
    @PutMapping(Relations.CURRENT_USER)
    @PreAuthorize("isFullyAuthenticated()")
    public AuthenticationResponse editCurrentUser(@AuthenticationPrincipal User user,
                                                  @RequestBody @Validated User editedUser,
                                                  HttpServletRequest request,
                                                  Authentication auth) {
        
        if (!editedUser.getAuthorities().equals(user.getAuthorities()) &&
                !user.getAuthorities().contains(Authority.USERS)) {
            throw new AccessDeniedException("Not authorized to change authorities");
        }
        
        editedUser.setId(user.getId());
        editedUser.setVersion(user.getVersion());
    
        if (editedUser.getSecret() != null) {
            editedUser.setSecret(passwordEncoder.encode(editedUser.getSecret()));
        } else {
            editedUser.setSecret(user.getSecret());
        }
        
        try {
            log.debug("Saving edited user: {}", editedUser);
            editedUser = userRepository.save(editedUser);
            log.debug("Saved edited user:  {}", editedUser);
        } catch (DataIntegrityViolationException e) {
            log.debug("Duplicate key: {}", e);
            throw new UserNameExistsException();
        } catch (OptimisticLockingFailureException e) {
            log.debug("Optimistic locking failure: ", e);
            throw new OptimisticLockingException();
        }
        
        logout(auth);
        return loginInternal(user, request);
    }
    
    private String generateToken() {
        final byte[] bytes = new byte[SecurityConstants.AUTH_TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(bytes);
        final String token = Base64.getEncoder().encodeToString(bytes).replace("=", "");
        return authenticationInfoRepository.get(token) != null ? generateToken() : token;
    }
}
