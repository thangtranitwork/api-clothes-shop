package com.clothes.noc.admin.controller;

import com.clothes.noc.dto.request.ColorRequest;
import com.clothes.noc.admin.service.ColorManageService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ColorResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/colors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class ColorManageController {
    private final ColorManageService colorManageService;

    @GetMapping
    ApiResponse<List<ColorResponse>> getAll() {
        return ApiResponse.<List<ColorResponse>>builder()
                .body(colorManageService.getAll())
                .build();
    }

    @PostMapping
    ApiResponse<ColorResponse> add(@RequestBody ColorRequest request) {
        return ApiResponse.<ColorResponse>builder()
                .body(colorManageService.add(request))
                .build();
    }

    @DeleteMapping
    ApiResponse<Void> delete(String code) {
        colorManageService.delete(code);
        return new ApiResponse<>();
    }
}