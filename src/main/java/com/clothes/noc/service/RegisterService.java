package com.clothes.noc.service;

import com.clothes.noc.dto.request.OAuth2RegisterRequest;
import com.clothes.noc.dto.request.RegisterRequest;
import com.clothes.noc.dto.response.AuthenticationResponse;
import com.clothes.noc.entity.Platform;
import com.clothes.noc.entity.User;
import com.clothes.noc.entity.VerifyCode;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.UserMapper;
import com.clothes.noc.repository.UserRepository;
import com.clothes.noc.repository.VerifyCodeRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterService {
    final UserRepository userRepository;
    final VerifyCodeRepository verifyCodeRepository;
    final PasswordEncoder passwordEncoder;
    final UserMapper userMapper;
    final EmailService emailService;
    final AuthenticationService authenticationService;

    @Value("${verify.email.duration}")
    int verifyEmailDuration;

    @Value("${FE_ORIGIN}")
    String feOrigin;

    static final String VERIFY_EMAIL_TEMPLATE = "verify-email";
    static final String VERIFY_EMAIL_SUBJECT = "Xác minh email";

    public void register(RegisterRequest request) {
        validateUserDoesNotExist(request.getEmail(), request.getPlatform());

        if (Platform.APP.name().equals(request.getPlatform())) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User user = userMapper.toUser(request);
        userRepository.save(user);

        VerifyCode verifyCode = createAndSaveVerifyCode(user);
        sendVerificationEmail(user, verifyCode);
    }

    private void deleteOldVerifyCode(User user) {
        if(user.getVerifyCode() != null) {
            verifyCodeRepository.delete(user.getVerifyCode());
            user.setVerifyCode(null);
        }
    }

    public void register(OAuth2RegisterRequest request) {
        validateUserDoesNotExist(request.getEmail(), request.getPlatform());
        User user = userMapper.toUser(request);
        userRepository.save(user);
    }

    private void validateUserDoesNotExist(String email, String platform) {
        userRepository.findByEmailAndPlatform(email, Platform.valueOf(platform)).ifPresent(u -> {
            if (u.isVerified()) {
                throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
            } else {
                throw new AppException(ErrorCode.USER_HAS_NOT_VERIFIED_EMAIL);
            }
        });
    }

    public VerifyCode createAndSaveVerifyCode(User user) {
        Date expiryTime = Date.from(Instant.now().plus(verifyEmailDuration, ChronoUnit.MINUTES));
        VerifyCode verifyCode = VerifyCode.builder()
                .code(UUID.randomUUID().toString())
                .user(user)
                .expiryTime(expiryTime)
                .build();
        verifyCodeRepository.save(verifyCode);
        return verifyCode;
    }

    public void sendVerificationEmail(User user, VerifyCode verifyCode) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("expiryDate", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(verifyCode.getExpiryTime()));
        variables.put("title", VERIFY_EMAIL_SUBJECT);
        variables.put("verificationLink", String.format("%s/account/?tab=register&email=%s&step=2&code=%s", feOrigin, user.getEmail(), verifyCode.getCode()));
        emailService.sendMail(user.getEmail(), VERIFY_EMAIL_SUBJECT, variables, VERIFY_EMAIL_TEMPLATE);
    }

    public AuthenticationResponse verify(String code, HttpServletResponse response) {
        try {
            UUID.fromString(code);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VERIFY_CODE_INVALID);
        }

        VerifyCode verifyCode = verifyCodeRepository.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.VERIFY_CODE_DOES_NOT_EXIST));

        if (verifyCode.getExpiryTime().before(new Date())) {
            throw new AppException(ErrorCode.VERIFY_CODE_TIMEOUT);
        }

        User user = verifyCode.getUser();
        user.setVerified(true);
        userRepository.save(user);
        verifyCodeRepository.delete(verifyCode);

        return authenticationService.generateResponse(user, response);
    }

    public void resend(String email) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (!user.isVerified()) {
            deleteOldVerifyCode(user);
            VerifyCode verifyCode = createAndSaveVerifyCode(user);
            sendVerificationEmail(verifyCode.getUser(), verifyCode);
        }
    }
}
