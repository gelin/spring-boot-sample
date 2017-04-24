package it.sevenbits.sample.springboot.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication provider which is able to verify JWT tokens.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtTokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());

        try {
            return tokenService.parseToken(token);
        } catch (Exception e) {
            throw new JwtAuthenticationException("Invalid token received", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtToken.class.isAssignableFrom(authentication));
    }

}
