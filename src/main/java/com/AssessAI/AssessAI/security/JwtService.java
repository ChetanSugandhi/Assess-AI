package com.AssessAI.AssessAI.security;

import com.AssessAI.AssessAI.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


        @Value("${app.jwt.secret}")
        private String secret;

        @Value("${app.jwt.expiration}")
        private long expirationMillis;

        public String generateToken(User user) {
            return buildToken(Map.of("role", user.getAuthorities()), user.getUsername(), expirationMillis);
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public boolean isTokenValid(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }

        // Helpers
        private String buildToken(Map<String, Object> extraClaims, String subject, long expirationMillis) {
            Date now = new Date();
            Date exp = new Date(now.getTime() + expirationMillis);
            return Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(exp)
                    .signWith(getSignInKey())
                    .compact();
        }

        private Key getSignInKey() {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        private <T> T extractClaim(String token, Function<Claims, T> resolver) {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return resolver.apply(claims);
        }
}

