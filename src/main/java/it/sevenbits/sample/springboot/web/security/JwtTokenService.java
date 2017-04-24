package it.sevenbits.sample.springboot.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * Service to generate and parse JWT tokens.
 */
@Service
public class JwtTokenService {

    private final JwtSettings settings;

    @Autowired
    public JwtTokenService(final JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Creates new Token for user.
     */
    public String createToken(Authentication authentication) {
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authentication.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));
        Date currentTime = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(currentTime)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return token;
    }

}
