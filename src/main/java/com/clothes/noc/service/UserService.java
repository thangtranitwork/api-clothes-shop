package com.clothes.noc.service;

import com.clothes.noc.dto.request.UserProfileRequest;
import com.clothes.noc.dto.response.UserProfileResponse;
import com.clothes.noc.enums.Platform;
import com.clothes.noc.entity.User;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.UserMapper;
import com.clothes.noc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserProfileResponse getProfile() {
        User user = userRepository.findById(getCurrentUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
        return userMapper.toUserProfileResponse(user);
    }


    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS));
    }

    public boolean checkExists(String email, Platform platform) {
        return userRepository.existsByEmailAndPlatform(email, platform);
    }

    public UserProfileResponse updateProfile(UserProfileRequest request) {
        User user = getCurrentUser();
        userMapper.updateUser(user, request);
        userRepository.save(user);
        return userMapper.toUserProfileResponse(user);
    }


    public String getCurrentUserId() {
        return jwtUtil.getCurrentUserId();
    }

    public User getCurrentUser() {
        return getUser(getCurrentUserId());
    }
}

