package it.sevenbits.sample.springboot.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Settings to the JWT token.
 */
@Component
public class JwtSettings {

    private String tokenIssuer;
    private String tokenSigningKey;

    public JwtSettings(@Value("${jwt.tokenIssuer}") final String tokenIssuer,
                       @Value("${jwt.tokenSigningKey}") final String tokenSigningKey) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.US_ASCII);
    }

    public Duration getTokenExpiredIn() {
        return Duration.ofHours(4);
        // Actually should be less: about minutes, and use refresh token to update.
    }

}
