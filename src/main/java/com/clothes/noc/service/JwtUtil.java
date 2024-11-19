package com.clothes.noc.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import com.clothes.noc.entity.LoggedOutToken;
import com.clothes.noc.entity.User;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.LoggedOutTokenRepository;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

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

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(User user, boolean isRefreshToken, String jit) {
        return createJwtToken(user, isRefreshToken, jit);
    }

    public String generateToken(User user, boolean isRefreshToken) {
        return generateToken(user, isRefreshToken, UUID.randomUUID().toString());
    }

    private String createJwtToken(User user, boolean isRefreshToken, String jit) {
        int duration = !isRefreshToken ? accessTokenDuration : refreshTokenDuration;
        ChronoUnit unit = !isRefreshToken ? ChronoUnit.MINUTES : ChronoUnit.DAYS;

        Instant now = Instant.now();

        return Jwts.builder()
                .setIssuer("stu-e-learning")
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(duration, unit)))
                .setId(jit)
                .claim("scope", user.getRole())
                .claim("type", isRefreshToken ? "refresh-token" : "access-token")
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims verifyToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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

        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getToken();
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));
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
