package com.ssafy.myalbba.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("JWT secretKey: " + secret);
    }

    public String getUserEmail(String token) {
        Claims claims = getClaims(token);
        return claims.get("userEmail", String.class);
    }

    public String getUserName(String token) {
        Claims claims = getClaims(token);
        return claims.get("userName", String.class);
    }

    public String getUserRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("userRole", String.class);
    }

    public Boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public String createJwt(String userEmail, String userName, String userRole, Long expiredMs) {
        return Jwts.builder()
                .claim("userEmail", userEmail)
                .claim("userName", userName)
                .claim("userRole", userRole)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
