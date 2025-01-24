package com.clothes.noc.admin.controller;

import com.clothes.noc.admin.service.ProductTypeManageService;
import com.clothes.noc.dto.request.ProductTypeRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ProductTypeResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class ProductTypesManageController {
    final ProductTypeManageService productTypeManageService;

    @GetMapping
    ApiResponse<List<ProductTypeResponse>> getAll() {
        return ApiResponse.<List<ProductTypeResponse>>builder()
                .body(productTypeManageService.getAll())
                .build();
    }

    @PostMapping
    ApiResponse<ProductTypeResponse> add(@RequestBody @Valid ProductTypeRequest productTypeRequest) {
        return ApiResponse.<ProductTypeResponse>builder()
                .body(productTypeManageService.add(productTypeRequest))
                .build();
    }

    @DeleteMapping
    ApiResponse<Void> delete(String id) {
        productTypeManageService.delete(id);
        return new ApiResponse<>();
    }
}
