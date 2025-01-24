package com.clothes.noc.admin.controller;


import com.clothes.noc.admin.service.ProductManageService;
import com.clothes.noc.dto.request.ProductCreationRequest;
import com.clothes.noc.dto.request.UpdatePriceRequest;
import com.clothes.noc.dto.response.AdminProductResponse;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ProductCreationResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class ProductManageController {
    final ProductManageService productManageService;

    @GetMapping("/{path}")
    ApiResponse<AdminProductResponse> get(@PathVariable String path) {
        return ApiResponse.<AdminProductResponse>builder()
                .body(productManageService.get(path))
                .build();
    }

    @PostMapping
    ApiResponse<ProductCreationResponse> addProduct(ProductCreationRequest productCreationRequest) {
        return ApiResponse.<ProductCreationResponse>builder()
                .body(productManageService.add(productCreationRequest))
                .build();
    }

    @PatchMapping("/{id}")
    ApiResponse<Void> updatePrice(@PathVariable String id,@RequestBody @Valid UpdatePriceRequest request) {
        productManageService.updatePrice(id, request);
        return new ApiResponse<>();
    }

    @DeleteMapping("/{id}")
    ApiResponse<ApiResponse<Void>> delete(@PathVariable String id) {
        productManageService.delete(id);
        return new ApiResponse<>();
    }
}
