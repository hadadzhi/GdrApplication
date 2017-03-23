package ru.cdfe.gdr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.dto.AuthenticationRequest;
import ru.cdfe.gdr.dto.AuthenticationResponse;
import ru.cdfe.gdr.constants.Constants;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.exceptions.security.BadCredentialsException;
import ru.cdfe.gdr.exceptions.security.SecurityException;
import ru.cdfe.gdr.repositories.UserRepository;
import ru.cdfe.gdr.security.AuthenticationInfo;
import ru.cdfe.gdr.security.AuthenticationInfoRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import static java.util.stream.Collectors.toSet;

@RestController
@Slf4j
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
    
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody @Validated AuthenticationRequest authRequest,
                                        HttpServletRequest httpRequest) {
        
        final User user = userRepository.findOne(authRequest.getName());
        
        if (user != null && passwordEncoder.matches(authRequest.getSecret(), user.getSecret())) {
            final Instant expiry = Instant.now().plus(Constants.AUTH_SESSION_LENGTH);
            final String token = generateToken();
            
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token,
                    user.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(toSet()));
            auth.setDetails(new WebAuthenticationDetails(httpRequest));
            
            authenticationInfoRepository.put(token, new AuthenticationInfo(auth, expiry));
            return new AuthenticationResponse(token);
        }
        
        throw new BadCredentialsException();
    }
    
    @PreAuthorize("isFullyAuthenticated()")
    @PostMapping("logout")
    public ResponseEntity logout(Authentication auth) {
        if (authenticationInfoRepository.remove(String.class.cast(auth.getCredentials())) == null) {
            log.error("logout() was called, but there is no authentication for the given token");
            throw new SecurityException();
        }
        return ResponseEntity.noContent().build();
    }
    
    private String generateToken() {
        final byte[] bytes = new byte[Constants.AUTH_TOKEN_LENGTH_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
