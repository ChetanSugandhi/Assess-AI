package com.AssessAI.AssessAI.security.jwt;

import com.AssessAI.AssessAI.models.User;
import com.AssessAI.AssessAI.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMS;

    @Value("${app.ecom.jwtCookie}")
    private String jwtCookie;


//    // Getting JWT from header --> we are uncomment this for swagger accessible..
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println(bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    // cookie se jwt token ko extract kr rhe..
    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        }
        else {
            return null;
        }
    }

    // ye method token generate kregi using getTokenFromUsername se and woh token ko cookie ke form mein client ko response mein bhej degi....
// this is for generate JWT Cookies..
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwtToken = getTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwtToken) // uss token se cookie banai...and jwtCookie mein store kra..
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
        return cookie;
    }


    // this is for clear the cookie... and remove the cookie from user
    public ResponseCookie clearJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null) // uss token se cookie banai...and jwtCookie mein store kra..
                .path("/api")
                .build();
        return cookie;
    }



    // Generate token from username
    public String getTokenFromUsername(String userName) {
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMS ))
                .signWith(key())
                .compact();
    }

    // Generate username from JWT token
    public String getUsernameFromJWTToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }


    // Generate signing key
    public Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        }
        catch (MalformedJwtException e) {
            System.out.println("Invalid JWT Token : " + e.getMessage());
        }
        catch (ExpiredJwtException e) {
            System.out.println("JWT Token is expired : " + e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            System.out.println("JWT Token is Unsupported : " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty : " + e.getMessage());
        }
        return false;
    }

}
