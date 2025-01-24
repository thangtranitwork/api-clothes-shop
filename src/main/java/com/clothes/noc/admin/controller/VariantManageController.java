package com.clothes.noc.admin.controller;

import com.clothes.noc.admin.service.VariantManageService;
import com.clothes.noc.dto.request.UpdateVariantQuantityRequest;
import com.clothes.noc.dto.request.VariantCreationRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ProductVariantResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class VariantManageController {
    final VariantManageService variantManageService;

    @PostMapping("{productId}/variants")
    ApiResponse<ProductVariantResponse> add(@PathVariable String productId, @Valid VariantCreationRequest request) {
        return ApiResponse.<ProductVariantResponse>builder()
                .body(variantManageService.addVariant(productId, request))
                .build();
    }

    @PatchMapping("/variants/{id}")
    ApiResponse<Void> updateQuantity(@PathVariable String id, @Valid UpdateVariantQuantityRequest request) {
        variantManageService.updateQuantity(id, request);
        return new ApiResponse<>();
    }

    @DeleteMapping("/variants/{id}")
    ApiResponse<Void> delete(@PathVariable String id) {
        variantManageService.delete(id);
        return new ApiResponse<>();
    }
}
