package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.web.security.JwtAuthFilter;
import it.sevenbits.sample.springboot.web.security.JwtAuthenticationProvider;
import it.sevenbits.sample.springboot.web.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.servlet.Filter;
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
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests().antMatchers("/login").permitAll()
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtLoginFilter loginFilter() throws Exception {
        return new JwtLoginFilter("/login", authenticationManager());
    }

    @Bean
    public JwtAuthFilter authFilter() throws Exception {
        return new JwtAuthFilter(AnyRequestMatcher.INSTANCE, authenticationManager());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .jdbcAuthentication().dataSource(dataSource)
        .passwordEncoder(passwordEncoder())
        .and()
        .authenticationProvider(jwtAuthenticationProvider());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

}
