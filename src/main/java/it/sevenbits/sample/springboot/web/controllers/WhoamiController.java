package it.sevenbits.sample.springboot.web.controllers;

import it.sevenbits.sample.springboot.core.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to list users.
 */
@Controller
@RequestMapping("/whoami")
public class WhoamiController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new User(authentication);
    }

}
