package com.clothes.noc.service;

import com.clothes.noc.enums.Platform;
import com.clothes.noc.entity.User;
import com.clothes.noc.entity.VerifyCode;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.UserRepository;
import com.clothes.noc.repository.VerifyCodeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordService {
    final UserRepository userRepository;
    final VerifyCodeRepository verifyCodeRepository;
    final EmailService emailService;
    final PasswordEncoder passwordEncoder;
    @Value("${verify.email.duration}")
    int verifyEmailDuration;
    @Value("${FE_ORIGIN}")
    private String feOrigin;
    static final String VERIFY_EMAIL_TEMPLATE = "verify-email";
    static final String VERIFY_EMAIL_SUBJECT = "Verify for update password";


    public void sendMail(String email) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        deleteOldVerifyCode(user);
        VerifyCode verifyCode = createAndSaveVerifyCode(user);
        sendVerificationEmail(user, verifyCode);
    }

    private void deleteOldVerifyCode(User user) {
        if (user.getVerifyCode() != null) {
            verifyCodeRepository.delete(user.getVerifyCode());
            user.setVerifyCode(null);
        }
    }

    public VerifyCode createAndSaveVerifyCode(User user) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(verifyEmailDuration);
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
        variables.put("expiryDate", verifyCode.getExpiryTime());
        variables.put("title", VERIFY_EMAIL_SUBJECT);
        variables.put("verificationLink", String.format("%s/account/?tab=change-password&email=%s&step=2&code=%s", feOrigin, user.getEmail(), verifyCode.getCode()));
        emailService.sendMail(user.getEmail(), VERIFY_EMAIL_SUBJECT, variables, VERIFY_EMAIL_TEMPLATE);
    }

    public void verify(String email, String code) {
        try {
            UUID.fromString(code);
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.VERIFY_CODE_INVALID);
        }

        User user = userRepository.findByEmailAndPlatform(email, Platform.APP)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        VerifyCode verifyCode = user.getVerifyCode();
        if (verifyCode == null) {
            throw new AppException(ErrorCode.VERIFY_CODE_INVALID);
        }

        if (verifyCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.VERIFY_CODE_TIMEOUT);
        }

        verifyCode.setVerified(true);
        verifyCodeRepository.save(verifyCode);
    }

    public void update(String email, String password) {
        User user = userRepository.findByEmailAndPlatform(email, Platform.APP)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        VerifyCode verifyCode = user.getVerifyCode();
        if (verifyCode == null || !verifyCode.isVerified()) {
            throw new AppException(ErrorCode.VERIFY_CODE_INVALID);
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
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
