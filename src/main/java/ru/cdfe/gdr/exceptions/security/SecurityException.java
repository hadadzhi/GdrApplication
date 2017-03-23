package ru.cdfe.gdr.exceptions.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General security fault.
 */
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class SecurityException extends RuntimeException {}
