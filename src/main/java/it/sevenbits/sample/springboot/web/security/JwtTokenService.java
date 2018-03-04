package it.sevenbits.sample.springboot.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.sevenbits.sample.springboot.core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to generate and parse JWT tokens.
 */
@Service
public class JwtTokenService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtSettings settings;

    public JwtTokenService(final JwtSettings settings) {
        this.settings = settings;
    }

    /**
     * Creates new Token for user.
     * @param authentication contains UserDetails to be represented as token
     * @return signed token
     */
    @Deprecated
    public String createToken(Authentication authentication) {
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
        Date currentTime = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(currentTime)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();

        return token;
    }

    /**
     * Creates new Token for user.
     * @param user contains User to be represented as token
     * @return signed token
     */
    public String createToken(User user) {
        logger.debug("Generating token for {}", user.getUsername());

        Instant now = Instant.now();

        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getUsername())
                .setExpiration(Date.from(now.plus(settings.getTokenExpiredIn())));
        claims.put("authorities", user.getAuthorities());

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
        return token;
    }

    /**
     * Parses the token
     * @param token the token string to parse
     * @return authenticated data
     */
    public Authentication parseToken(String token) {
        // TODO
        Jws<Claims> claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);

        String subject = claims.getBody().getSubject();
        List<String> roles = claims.getBody().get("roles", List.class);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AuthenticatedJwtToken(subject, authorities);
    }

}
