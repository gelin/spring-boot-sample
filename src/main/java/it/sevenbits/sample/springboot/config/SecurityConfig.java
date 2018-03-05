package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.web.security.JwtAuthFailureHandler;
import it.sevenbits.sample.springboot.web.security.JwtAuthFilter;
import it.sevenbits.sample.springboot.web.security.JwtAuthSuccessHandler;
import it.sevenbits.sample.springboot.web.security.JwtAuthenticationProvider;
import it.sevenbits.sample.springboot.web.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Spring Security configuration.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier("itemsDataSource")
//    private DataSource usersDataSource;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.formLogin().disable();
        http.logout().disable();
        http.sessionManagement().disable();
        http.requestCache().disable();
        http.anonymous();

        //...
        RequestMatcher authFilterRequests = request -> {
            String path = request.getServletPath() + request.getPathInfo();
            return !path.startsWith("/login");  // all paths except /login
        };
        JwtAuthFilter authFilter = new JwtAuthFilter(authFilterRequests);
        authFilter.setAuthenticationSuccessHandler(new JwtAuthSuccessHandler());
        authFilter.setAuthenticationFailureHandler(new JwtAuthFailureHandler());
        http.addFilterBefore(authFilter, FilterSecurityInterceptor.class);

        //...
        http.authorizeRequests().antMatchers("/login").permitAll()
//        .and()
//        .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
        .and()
        .authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(usersDataSource)
//        .passwordEncoder(new BCryptPasswordEncoder());

        auth.authenticationProvider(new JwtAuthenticationProvider(jwtTokenService));
    }

}
