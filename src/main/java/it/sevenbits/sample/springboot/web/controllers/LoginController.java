package it.sevenbits.sample.springboot.web.controllers;

import it.sevenbits.sample.springboot.core.model.User;
import it.sevenbits.sample.springboot.core.services.LoginService;
import it.sevenbits.sample.springboot.web.models.Login;
import it.sevenbits.sample.springboot.web.models.Token;
import it.sevenbits.sample.springboot.web.security.JwtTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Performs login action.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;

    public LoginController(final LoginService loginService, final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Token create(@RequestBody Login login) {
        User user = loginService.login(login);
        String token = tokenService.createToken(user);
        return new Token(token);
    }

}
