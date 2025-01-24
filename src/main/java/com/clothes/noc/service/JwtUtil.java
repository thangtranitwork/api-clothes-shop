package com.clothes.noc.service;

import com.clothes.noc.entity.LoggedOutToken;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.LoggedOutTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.signerKey}")
    private String secretKey;

    @Value("${jwt.accessToken.duration}")
    private int accessTokenDuration;

    @Value("${jwt.refreshToken.duration}")
    private int refreshTokenDuration;

    private final LoggedOutTokenRepository loggedOutTokenRepository;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String id, String role, boolean isRefreshToken, String jit) {
        return createJwtToken(id, role, isRefreshToken, jit);
    }

    private String createJwtToken(String id, String role, boolean isRefreshToken, String jit) {
        int duration = !isRefreshToken ? accessTokenDuration : refreshTokenDuration;
        ChronoUnit unit = !isRefreshToken ? ChronoUnit.MINUTES : ChronoUnit.DAYS;

        Instant now = Instant.now();

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuer("noc-clothes")
                .subject(String.valueOf(id))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(duration, unit)))
                .id(jit)
                .claim("scope", role)
                .claim("type", isRefreshToken ? "refresh-token" : "access-token")
                .signWith(getSigningKey())
                .compact();
    }

    public Claims verifyToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
    }

    public String getSub(String token) {
        Claims claims = verifyToken(token);
        return claims.getSubject();
    }

    public String getJit(String token) {
        Claims claims = verifyToken(token);
        return claims.getId();
    }

    public String getCurrentUserId() {
        return getJwt().getSubject();
    }

    private Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken authenticationToken) {
            return authenticationToken.getToken();
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public boolean isRefreshToken(String token) {
        Claims claims = verifyToken(token);
        return "refresh-token".equals(claims.get("type"));
    }

    public void logoutToken(String token) {
        Claims claims = verifyToken(token);
        String jit = claims.getId();
        Date expiryTime = claims.getExpiration();

        LoggedOutToken loggedOutToken = LoggedOutToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        loggedOutTokenRepository.save(loggedOutToken);
    }
}