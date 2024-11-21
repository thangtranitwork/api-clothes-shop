package com.clothes.noc.service;

import com.clothes.noc.dto.request.AuthenticationRequest;
import com.clothes.noc.dto.request.IntrospectRequest;
import com.clothes.noc.dto.response.AuthenticationResponse;
import com.clothes.noc.dto.response.IntrospectResponse;
import com.clothes.noc.entity.Platform;
import com.clothes.noc.entity.User;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtil jwtUtil;
    int MAX_FAILED_ATTEMPTS = 5;
    @Value("${jwt.refreshToken.duration}")
    @NonFinal
    int REFRESH_TOKEN_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmailAndPlatform(request.getEmail(), Platform.valueOf(request.getPlatform()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (!user.isVerified()) {
            throw new AppException(ErrorCode.USER_HAS_NOT_VERIFIED_EMAIL);
        }

        if (isAccountLocked(user)) {
            throw new AppException(ErrorCode.THIS_USER_HAS_BEEN_LOCKED, Map.of("lockoutTime", user.getLockoutTime()));
        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            processFailedLoginAttempt(user);
            throw new AppException(ErrorCode.LOGIN_FAILED, Map.of("remainingTry", MAX_FAILED_ATTEMPTS - user.getFailedAttempts()));
        }
        resetFailedAttempts(user);
        return generateResponse(user, response);
    }

    public AuthenticationResponse oauth2LoginAuthenticate(AuthenticationRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmailAndPlatform(request.getEmail(), Platform.valueOf(request.getPlatform()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return generateResponse(user, response);
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }

        User user = userRepository.findById(jwtUtil.getSub(refreshToken))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return AuthenticationResponse.builder()
                .accessToken(jwtUtil.generateToken(user, false, jwtUtil.getJit(refreshToken)))
                .build();
    }

    public AuthenticationResponse generateResponse(User user, HttpServletResponse response) {
        String id = UUID.randomUUID().toString();
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtil.generateToken(user, true, id));
        refreshTokenCookie.setHttpOnly(true); // Đảm bảo HttpOnly để bảo mật
        refreshTokenCookie.setSecure(false);   // Chỉ gửi qua HTTPS
        refreshTokenCookie.setPath("/");      // Áp dụng cho tất cả các đường dẫn
        refreshTokenCookie.setMaxAge(REFRESH_TOKEN_DURATION * 24 * 60 * 60); // Thời gian sống của cookie (7 ngày)
        response.addCookie(refreshTokenCookie);
        return AuthenticationResponse.builder()
                .accessToken(jwtUtil.generateToken(user, false, id))
                .build();
    }

    public void logout(String refreshToken, HttpServletResponse response) {
            jwtUtil.logoutToken(refreshToken);
            Cookie refreshTokenCookie = new Cookie("refreshToken", "");
            refreshTokenCookie.setHttpOnly(true); // Đảm bảo HttpOnly để bảo mật
            refreshTokenCookie.setSecure(false);   // Chỉ gửi qua HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0);
            response.addCookie(refreshTokenCookie);
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        if (jwtUtil.isRefreshToken(request.getAccessToken())) {
            throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_OR_INVALID);
        }
        User user = userRepository.findById(jwtUtil.getSub(request.getAccessToken()))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return IntrospectResponse.builder()
                .valid(true)
                .id(user.getId())
                .role(user.getRole().toString())
                .build();
    }

    public boolean isAccountLocked(User user) {
        if (user.isAccountLocked()) {
            if (user.getLockoutTime().isBefore(LocalDateTime.now())) {
                resetFailedAttempts(user);
                return false;
            }
            return true;
        }
        return false;
    }

    public void processFailedLoginAttempt(User user) {

        user.setFailedAttempts(user.getFailedAttempts() + 1);

        if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
            user.setLockoutTime(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            if (user.getFailedAttempts() == MAX_FAILED_ATTEMPTS) {
                throw new AppException(ErrorCode.THIS_USER_HAS_BEEN_LOCKED, Map.of("lockoutTime", user.getLockoutTime()));
            }
        }

        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.setFailedAttempts(0);
        user.setAccountLocked(false);
        user.setLockoutTime(null);
        userRepository.save(user);
    }

}

