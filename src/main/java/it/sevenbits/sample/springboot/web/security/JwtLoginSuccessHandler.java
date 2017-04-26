package it.sevenbits.sample.springboot.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sevenbits.sample.springboot.web.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Is called on success login.
 * Generates JWT token.
 */
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService tokenService;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtLoginSuccessHandler(final JwtTokenService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token = tokenService.createToken(authentication);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), new Token(token));
//        response.getWriter().println("{\n" +
//                "  \"status\": 200,\n" +
//                "  \"message\": \"Login successful.\"\n" +
//                "}");
    }
}
