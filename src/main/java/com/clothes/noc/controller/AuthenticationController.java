package com.clothes.noc.controller;

import com.clothes.noc.dto.request.AuthenticationRequest;
import com.clothes.noc.dto.request.IntrospectRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.AuthenticationResponse;
import com.clothes.noc.dto.response.IntrospectResponse;
import com.clothes.noc.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    final AuthenticationService authenticationService;
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request, HttpServletResponse response) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(authenticationService.authenticate(request, response))
                .build();
    }

    @DeleteMapping("/logout")
    ApiResponse<Void> logout(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        if(refreshToken.isEmpty()) {
            return null;
        }
        authenticationService.logout(refreshToken, response);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .body(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken) {
        return ApiResponse.<AuthenticationResponse>builder()
                .body(authenticationService.refreshToken(refreshToken))
                .build();
    }
}
