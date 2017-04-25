package it.sevenbits.sample.springboot.web.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to take JwtToken from the request header.
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static final Pattern BEARER_AUTH_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    private final AuthenticationFailureHandler failureHandler;

    public JwtAuthFilter(RequestMatcher matcher, AuthenticationManager authenticationManager,
                         AuthenticationFailureHandler failureHandler) {
        super(matcher);
        setAuthenticationManager(authenticationManager);
        this.failureHandler = failureHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String token;
        try {
            String authHeader = request.getHeader("Authorization");
            // extract token from header...
            Matcher m = BEARER_AUTH_PATTERN.matcher(authHeader);
            if (m.matches()) {
                token = m.group(TOKEN_GROUP);
            } else {
                throw new JwtAuthenticationException("Invalid Authorization header: " + authHeader);
            }
        } catch (Exception e) {
            throw new JwtAuthenticationException("Failed to get Authorization header", e);
        }
        return getAuthenticationManager().authenticate(new JwtToken(token));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
