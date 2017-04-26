package it.sevenbits.sample.springboot.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple User class.
 */
public class User extends ResourceSupport {

    private final String username;
    private final List<String> roles;

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("roles") List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public User(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        roles = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

}
