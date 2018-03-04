package it.sevenbits.sample.springboot.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to take JwtToken from the request header.
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final Pattern BEARER_AUTH_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    public JwtAuthFilter(RequestMatcher matcher) {
        super(matcher);
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
            logger.warn("Failed to get Authorization header: {}", e.getMessage());
            return anonymousToken();
        }
        return new JwtToken(token);
    }

    private Authentication anonymousToken() {
        return new AnonymousAuthenticationToken("ANONYMOUS", "ANONYMOUS",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
