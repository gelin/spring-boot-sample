package it.sevenbits.sample.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sevenbits.sample.springboot.web.security.JwtAuthFailureHandler;
import it.sevenbits.sample.springboot.web.security.JwtAuthFilter;
import it.sevenbits.sample.springboot.web.security.JwtLoginFailureHandler;
import it.sevenbits.sample.springboot.web.security.JwtLoginFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import javax.sql.DataSource;

/**
 * Spring Security configuration.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //...

    private final DataSource usersDataSource;
    private final AuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationSuccessHandler jwtLoginSuccessHandler;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            @Qualifier("itemsDataSource") final DataSource usersDataSource,
            AuthenticationProvider jwtAuthenticationProvider,
            AuthenticationSuccessHandler jwtLoginSuccessHandler,
            ObjectMapper objectMapper) {
        this.usersDataSource = usersDataSource;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtLoginSuccessHandler = jwtLoginSuccessHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.sessionManagement().disable();
        http.anonymous();
        
        http.authorizeRequests().antMatchers("/login").permitAll()
//        .and()
//        .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
        .and()
        .authorizeRequests().anyRequest().authenticated();

//        http.addFilterBefore(authFilter(), FilterSecurityInterceptor.class);
    }

    private JwtLoginFilter loginFilter() throws Exception {
        return new JwtLoginFilter("/login", authenticationManager(),
                jwtLoginSuccessHandler, new JwtLoginFailureHandler(), objectMapper);
    }

    private JwtAuthFilter authFilter() throws Exception {
        return new JwtAuthFilter(AnyRequestMatcher.INSTANCE, authenticationManager(),
                new JwtAuthFailureHandler());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .jdbcAuthentication().dataSource(usersDataSource)
        .passwordEncoder(passwordEncoder())
        .and()
        .authenticationProvider(jwtAuthenticationProvider);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
