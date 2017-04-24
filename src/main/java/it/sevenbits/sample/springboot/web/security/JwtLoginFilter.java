package it.sevenbits.sample.springboot.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sevenbits.sample.springboot.web.models.Login;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter which verifies login/password and generates JWT token.
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtLoginSuccessHandler successHandler;
    private final JwtLoginFailureHandler failureHandler;

    public JwtLoginFilter(String defaultProcessUrl, AuthenticationManager authenticationManager) {
        super(defaultProcessUrl);
        setAuthenticationManager(authenticationManager);
        successHandler = new JwtLoginSuccessHandler();
        failureHandler = new JwtLoginFailureHandler();
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
