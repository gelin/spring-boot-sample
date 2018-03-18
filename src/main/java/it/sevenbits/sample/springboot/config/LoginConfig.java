package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.core.services.LoginService;
import it.sevenbits.sample.springboot.web.controllers.CookieLoginController;
import it.sevenbits.sample.springboot.web.security.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    @Bean
    public Object loginController(
        final LoginService loginService, final JwtTokenService tokenService
    ) {
//        return new BodyLoginController(loginService, tokenService);
        return new CookieLoginController(loginService, tokenService);
    }

}
