package com.clothes.noc.controller;

import com.clothes.noc.dto.request.UpdatePasswordRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.service.UpdatePasswordService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = {"/update-password", "/forgot-password"})
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordController {
    final UpdatePasswordService updatePasswordService;
    @GetMapping("/{email}")
    ApiResponse<Void> updatePassword(@PathVariable String email) {
        updatePasswordService.sendMail(email);
        return new ApiResponse<>();
    }

    @PutMapping("/verify/{email}")
    ApiResponse<Void> verify(@PathVariable String email, @RequestParam(name = "code") String code) {
        updatePasswordService.verify(email, code);
        return new ApiResponse<>();
    }

    @PutMapping("/change/{email}")
    ApiResponse<Void> changePassword(@PathVariable String email, @RequestBody @Valid UpdatePasswordRequest request) {
        updatePasswordService.update(email, request.getPassword());
        return new ApiResponse<>();
    }
    @GetMapping("/resend/{email}")
    ApiResponse<Void> changePassword(@PathVariable String email) {
        updatePasswordService.resend(email);
        return new ApiResponse<>();
    }
}
