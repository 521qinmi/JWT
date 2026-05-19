package com.gse.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final String SECRET = "replace-with-rsa-public-key-verify-in-real-system";

    public void validate(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT");
        }
    }

    public String extractJti(String jwt) {
        Claims c = Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return c.getId() != null ? c.getId() : UUID.randomUUID().toString();
    }
}