package com.clothes.noc.controller;

import com.clothes.noc.dto.request.IntrospectRequest;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.service.AuthenticationService;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    String signerKey;

    final AuthenticationService authenticationService;

    SecretKey secretKey;

    // Phương thức để giải mã JWT
    public Jwt decode(String token) {
        // Xác thực token (hoặc introspection) trước khi decode
        authenticationService.introspect(
                IntrospectRequest.builder()
                        .accessToken(token)
                        .build());

        // Khởi tạo secretKey từ signerKey nếu chưa tồn tại
        if (Objects.isNull(secretKey)) {
            secretKey = Keys.hmacShaKeyFor(signerKey.getBytes(StandardCharsets.UTF_8));
        }

        // Giải mã và xác thực JWT sử dụng jjwt
        try {
            return Jwt.withTokenValue(token).build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);  // Ném ngoại lệ khi token không hợp lệ
        }
    }
}
