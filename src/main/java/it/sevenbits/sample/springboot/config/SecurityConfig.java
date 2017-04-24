package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.web.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        .authorizeRequests().antMatchers("/login").permitAll()
        .and()
        .authorizeRequests().anyRequest().fullyAuthenticated()
        .and()
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private JwtLoginFilter loginFilter() throws Exception {
        return new JwtLoginFilter("/login", authenticationManager());
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
