package it.sevenbits.sample.springboot.core.services;

import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends AuthenticationException {

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(String message) {
        super(message);
    }

}
