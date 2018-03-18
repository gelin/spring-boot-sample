package it.sevenbits.sample.springboot.web.controllers;

import it.sevenbits.sample.springboot.core.model.User;
import it.sevenbits.sample.springboot.core.services.LoginService;
import it.sevenbits.sample.springboot.web.models.Login;
import it.sevenbits.sample.springboot.web.models.Result;
import it.sevenbits.sample.springboot.web.models.Token;
import it.sevenbits.sample.springboot.web.security.JwtTokenService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Performs login action.
 */
@RequestMapping("/login")
public class CookieLoginController {

    private final LoginService loginService;
    private final JwtTokenService tokenService;

    public CookieLoginController(final LoginService loginService, final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Result<String> create(@RequestBody Login login, HttpServletResponse response) {
        User user = loginService.login(login);
        String token = tokenService.createToken(user);
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)(tokenService.getTokenExpiredIn().toMillis() / 1000));
        response.addCookie(cookie);
        return new Result<>("OK");
    }

}
