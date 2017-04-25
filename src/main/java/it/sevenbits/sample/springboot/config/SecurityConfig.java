package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.web.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Autowired
    public SecurityConfig(
            @Qualifier("itemsDataSource") final DataSource usersDataSource,
            AuthenticationProvider jwtAuthenticationProvider,
            AuthenticationSuccessHandler jwtLoginSuccessHandler) {
        this.usersDataSource = usersDataSource;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtLoginSuccessHandler = jwtLoginSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests().antMatchers("/login").permitAll()
//        .and()
//        .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
        .addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private JwtLoginFilter loginFilter() throws Exception {
        return new JwtLoginFilter("/login", authenticationManager(),
                jwtLoginSuccessHandler, new JwtLoginFailureHandler());
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
