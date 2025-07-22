package com.poc.profilehub.utils;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.poc.profilehub.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
	@Value("${jwt.secret:YourSuperSecretKeyThatShouldBeLongEnough12345}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        String roleString = extractClaim(token, claims -> claims.get("role", String.class));
        return roleString != null ? Role.valueOf(roleString) : null;
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("tokenType", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, String email) {
        try {
            final String tokenEmail = extractEmail(token);
            final String tokenType = extractTokenType(token);
            
            return tokenEmail.equals(email) 
                && !isTokenExpired(token)
                && "ACCESS".equals(tokenType);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | 
                 SignatureException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | 
                 SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}
