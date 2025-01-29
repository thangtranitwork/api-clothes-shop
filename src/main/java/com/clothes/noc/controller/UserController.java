package com.clothes.noc.controller;

import com.clothes.noc.dto.request.UserProfileRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.UserProfileResponse;
import com.clothes.noc.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    final UserService userService;

    @GetMapping("/profile")
    ApiResponse<UserProfileResponse> getProfile(){
        return ApiResponse.<UserProfileResponse>builder()
                .body(userService.getProfile())
                .build();
    }

    @PutMapping("/update-profile")
    ApiResponse<UserProfileResponse> updateProfile(@RequestBody @Valid UserProfileRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .body(userService.updateProfile(request))
                .build();
    }
}
