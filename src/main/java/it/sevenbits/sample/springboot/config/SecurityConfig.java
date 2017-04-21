package it.sevenbits.sample.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Spring Security configuration.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //...

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .and()
        .authorizeRequests().anyRequest().fullyAuthenticated()
        // all requests must be authorized to fully authenticated users (not "remembered")
        .and()
        .formLogin().usernameParameter("username").passwordParameter("password")
        // enable form authentication with specified form parameter names
        .successHandler(successHandler())
        .failureHandler(failureHandler())
        .loginProcessingUrl("/login").permitAll()
        // define URL to verify credentials and allow access to it
        .and()
        .logout().logoutUrl("/logout").permitAll();
        // define logout URL and allow access to it
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request,
                                 HttpServletResponse response, AuthenticationException exception)
                    throws IOException, ServletException {
                response.setContentType("application/json");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "POST /login to authorize.");
            }
        };
    }

    private AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {
                response.setContentType("application/json");
                response.getWriter().println("{\n" +
                        "  \"status\": 200,\n" +
                        "  \"message\": \"Login successful.\"\n" +
                        "}");
            }
        };
    }

    private AuthenticationFailureHandler failureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response, AuthenticationException exception)
                    throws IOException, ServletException {
                response.setContentType("application/json");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login and password.");
            }
        };
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
            .passwordEncoder(passwordEncoder());
                // find users in database
//                .withUser("admin").password("admin").roles("ADMIN", "USER")
                // create admin user
//                .and().withUser("user").password("user").roles("USER");
                // create ordinary user
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
