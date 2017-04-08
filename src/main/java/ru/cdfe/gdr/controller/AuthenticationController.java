package ru.cdfe.gdr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.GdrSecurityProperties;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.domain.security.dto.AuthenticationRequest;
import ru.cdfe.gdr.domain.security.dto.AuthenticationResponse;
import ru.cdfe.gdr.exception.AuthenticationException;
import ru.cdfe.gdr.exception.OptimisticLockingException;
import ru.cdfe.gdr.exception.UserNameExistsException;
import ru.cdfe.gdr.repository.UserRepository;
import ru.cdfe.gdr.security.TokenAuthentication;
import ru.cdfe.gdr.security.TokenAuthenticationRepository;
import ru.cdfe.gdr.security.annotation.TokenAuthenticated;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping
public class AuthenticationController {
    private final TokenAuthenticationRepository tokenAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final GdrSecurityProperties securityProperties;
    
    @Autowired
    public AuthenticationController(TokenAuthenticationRepository tokenAuthenticationRepository,
                                    PasswordEncoder passwordEncoder,
                                    UserRepository userRepository,
                                    GdrSecurityProperties securityProperties) {
        
        this.tokenAuthenticationRepository = tokenAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.securityProperties = securityProperties;
    }
    
    @PostMapping(Relations.LOGIN)
    @PreAuthorize("permitAll()")
    public AuthenticationResponse login(@RequestBody @Validated AuthenticationRequest authRequest,
                                        HttpServletRequest httpRequest)
            throws UnknownHostException {
        
        final User user = userRepository.findByName(authRequest.getName());
        
        if (user != null && passwordEncoder.matches(authRequest.getSecret(), user.getSecret())) {
            if (!user.getAllowedAddresses().isEmpty()) {
                final Set<InetAddress> userAddresses = new HashSet<>();
                
                for (final String a : user.getAllowedAddresses()) {
                    try {
                        userAddresses.addAll(Arrays.asList(InetAddress.getAllByName(a)));
                    } catch (UnknownHostException e) {
                        log.warn("User {} has malformed access address: {}", user.getName(), a);
                    }
                }
                
                if (!userAddresses.contains(InetAddress.getByName(httpRequest.getRemoteAddr()))) {
                    throw new AuthenticationException("Access denied from " + httpRequest.getRemoteHost());
                }
            }
            
            return new AuthenticationResponse(logUserIn(user, httpRequest));
        }
        
        log.info("Login failure: {}", authRequest.getName());
        throw new AuthenticationException();
    }
    
    private String logUserIn(User user, HttpServletRequest httpRequest) {
        final Instant expiry = Instant.now().plus(securityProperties.getTokenExpiry());
        final String token = generateToken();
        
        tokenAuthenticationRepository.put(new TokenAuthentication(token,
                user, httpRequest.getRemoteAddr(), expiry));
        
        log.info("Login success: {}@{}", user.getName(), httpRequest.getRemoteHost());
        return token;
    }
    
    @PostMapping(Relations.LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @TokenAuthenticated
    public void logout(TokenAuthentication auth) {
        if (!tokenAuthenticationRepository.remove(auth)) {
            log.error("Logout: authentication repository did not contain authentication: {}", auth);
        } else {
            log.info("Logout success: {}", auth.getPrincipal());
        }
    }
    
    @GetMapping(Relations.CURRENT_USER)
    @TokenAuthenticated
    public Resource<User> currentUser(@AuthenticationPrincipal User user) {
        return new Resource<>(user, linkTo(AuthenticationController.class)
                .slash(Relations.CURRENT_USER).withSelfRel());
    }
    
    @PutMapping(Relations.CURRENT_USER)
    @TokenAuthenticated
    public AuthenticationResponse editCurrentUser(@AuthenticationPrincipal User user,
                                                  @RequestBody @Validated User editedUser,
                                                  HttpServletRequest request,
                                                  TokenAuthentication auth) {
        
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
        return new AuthenticationResponse(logUserIn(user, request));
    }
    
    private String generateToken() {
        final byte[] bytes = new byte[securityProperties.getTokenLength()];
        secureRandom.nextBytes(bytes);
        final String token = Base64.getEncoder().encodeToString(bytes).replace("=", "");
        return tokenAuthenticationRepository.get(token) != null ? generateToken() : token;
    }
}
