package com.clothes.noc.controller;

import com.clothes.noc.dto.request.RegisterRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.AuthenticationResponse;
import com.clothes.noc.service.RegisterService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterController {
    final RegisterService registerService;
    @PostMapping("")
    ApiResponse<Void> addUser(@RequestBody @Valid RegisterRequest request) {
        registerService.register(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/verify")
    ApiResponse<AuthenticationResponse> verifyEmail(
            @RequestParam String code,
            HttpServletResponse response) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(registerService.verify(code, response))
                .build();
    }

    @GetMapping("/resend")
    ApiResponse<Void> resendCode(
            @RequestParam String email){
        registerService.resend(email);
        return new ApiResponse<>();
    }
}
