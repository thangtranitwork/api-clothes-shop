package com.clothes.noc.admin.controller;

import com.clothes.noc.admin.service.SizeManageService;
import com.clothes.noc.dto.request.SizeCreationRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.SizeResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/sizes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class SizeManageController {
    private final SizeManageService sizeManageService;
    @GetMapping
    ApiResponse<List<SizeResponse>> getAll() {
        return ApiResponse.<List<SizeResponse>>builder()
                .body(sizeManageService.getAll())
                .build();
    }

    @PostMapping
    ApiResponse<SizeResponse> add(@RequestBody SizeCreationRequest request) {
        return ApiResponse.<SizeResponse>builder()
                .body(sizeManageService.add(request))
                .build();
    }

    @DeleteMapping
    ApiResponse<Void> delete(String name) {
        sizeManageService.delete(name);
        return new ApiResponse<>();
    }
}
