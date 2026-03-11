package com.project.ecommarcemodernapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtProvider {

    @Value("${app.jwt.secret:bXlTZWN1cmVTZWNyZXRLZXlGb3JKV1RUb2tlbkdlbmVyYXRpb25BbmRWYWxpZGF0aW9uUHVycG9zZXNPbmx5MTIzNDU2Nzg5MA==}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    public String generateToken(Authentication authentication) {
        return Optional.of(authentication)
                .map(auth -> (UserDetails) auth.getPrincipal())
                .map(UserDetails::getUsername)
                .map(this::generateTokenFromUsername)
                .orElseThrow(() -> {
                    log.error("Error generating token: Authentication or principal is null");
                    return new JwtException("Failed to generate JWT token");
                });
    }

    public String generateTokenFromUsername(String username) {
        try {
            Instant now = Instant.now();
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plusMillis((int) jwtExpirationMs)))
                    .signWith(key())
                    .compact();
        } catch (Exception e) {
            log.error("Error building JWT token: {}", e.getMessage());
            throw new JwtException("Failed to build JWT token", e);
        }
    }

    public String getUserNameFromJwt(String token) {
        return parseToken(token)
                .map(Claims::getSubject)
                .orElseThrow(() -> {
                    log.error("Invalid JWT token: Cannot extract subject");
                    return new JwtException("Failed to extract username from token");
                });
    }

    public boolean validateJwtToken(String authToken) {
        try {
            return parseToken(authToken).isPresent();
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Cannot validate JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Optional<Claims> parseToken(String token) {
        try {
            return Optional.ofNullable(
                    Jwts.parser()
                            .verifyWith(key())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload()
            );
        } catch (JwtException e) {
            log.debug("Failed to parse JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private SecretKey key() {
        byte[] decodedKey = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }
}



