package it.sevenbits.sample.springboot.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Shows message how to login.
 */
public class JwtAuthFailureHandler implements AuthenticationFailureHandler {
    
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "POST /login to authorize.");
    }

    private void saveException(HttpServletRequest request, AuthenticationException exception) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        }
    }

}
