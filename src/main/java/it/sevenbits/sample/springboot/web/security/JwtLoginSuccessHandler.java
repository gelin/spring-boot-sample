package it.sevenbits.sample.springboot.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Is called on success login.
 * Generates JWT token.
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.setContentType("application/json");
        response.getWriter().println("{\n" +
                "  \"status\": 200,\n" +
                "  \"message\": \"Login successful.\"\n" +
                "}");
    }
}
