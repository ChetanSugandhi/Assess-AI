package com.AssessAI.AssessAI.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {

    private final String jwtSecret = "superSecretKeyForAssessAIChangeThis123456789";
    private final int jwtExpirationMs = 86400000; // 1 day

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT Token
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract Username
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate Token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Cookie version: set token as HttpOnly cookie
    public ResponseCookie generateJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)     // change to true in production HTTPS
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .build();
    }

    // Used during logout — MUST return ResponseCookie (NOT jakarta.Cookie)
    public ResponseCookie clearJwtCookie() {
        return ResponseCookie.from("jwt", null)
                .path("/")
                .maxAge(0)
                .build();
    }
}
