package com.threepmanagerapi.threepmanagerapi.settings.service;

import com.threepmanagerapi.threepmanagerapi.settings.config.JwtUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", jwtUser.getUserID());
        claims.put("name", jwtUser.getName());
        claims.put("email", jwtUser.getEmail());

        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            // Check if the token has expired
            if (isTokenExpired(token)) {
                return false;
            }

            // Check the token's signature
            byte[] signingKey = Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec key = new SecretKeySpec(signingKey, SignatureAlgorithm.HS256.getJcaName());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            // If no exceptions are thrown, the token is valid
            return true;
        } catch (ExpiredJwtException | MalformedJwtException  e) {
            // Token is expired or has an invalid format or signature
            return false;
        }
    }
    public Long extractuserID(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
            }

            Claims claims = extractAllClaims(token);
            return claims.get("userID", Long.class);

        } catch (ExpiredJwtException | MalformedJwtException e) {
            // Handle exceptions if needed (e.g., log or return a default value)
            return null; // Return null if there's an issue with the token
        }
    }
    public String extractEmail(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer " prefix
            }

            Claims claims = extractAllClaims(token);
            return claims.get("email", String.class);

        } catch (ExpiredJwtException | MalformedJwtException e) {
            // Handle exceptions if needed (e.g., log or return a default value)
            return null; // Return null if there's an issue with the token
        }
    }
}
