package it.sevenbits.sample.springboot.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Settings to the JWT token.
 */
@Component
public class JwtSettings {

    @Value("${jwt.tokenIssuer}")
    private String tokenIssuer;
    @Value("${jwt.tokenSigningKey}")
    private String tokenSigningKey;

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.US_ASCII);
    }

}
