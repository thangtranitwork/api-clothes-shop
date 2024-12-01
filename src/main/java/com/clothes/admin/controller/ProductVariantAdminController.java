package com.clothes.admin.controller;

import com.clothes.admin.service.ProductVariantAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ProductVariantResponse;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/product-variants")
public class ProductVariantAdminController {
    private final ProductVariantAdminService productVariantService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductVariantAdminController(ProductVariantAdminService productVariantService, ProductMapper productMapper) {
        this.productVariantService = productVariantService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ApiResponse<ProductVariantResponse> createProductVariant(@RequestBody ProductVariant productVariant) {
        ProductVariant savedVariant = productVariantService.save(productVariant);
        return ApiResponse.<ProductVariantResponse>builder()
                .message("ProductVariant created successfully")
                .body(productMapper.toProductVariantResponse(savedVariant))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductVariantResponse> getProductVariant(@PathVariable String id) {
        ProductVariant productVariant = productVariantService.findById(id);
        return ApiResponse.<ProductVariantResponse>builder()
                .message("ProductVariant found")
                .body(productMapper.toProductVariantResponse(productVariant))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductVariantResponse>> getAllProductVariants() {
        List<ProductVariant> variants = productVariantService.findAll();
        List<ProductVariantResponse> responses = variants.stream()
                .map(productMapper::toProductVariantResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .message("ProductVariants retrieved")
                .body(responses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductVariantResponse> updateProductVariant(@PathVariable String id, @RequestBody ProductVariant productVariant) {
        ProductVariant updatedVariant = productVariantService.update(id, productVariant);
        return ApiResponse.<ProductVariantResponse>builder()
                .message("ProductVariant updated")
                .body(productMapper.toProductVariantResponse(updatedVariant))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductVariant(@PathVariable String id) {
        productVariantService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("ProductVariant deleted successfully")
                .body(null)
                .build();
    }

    @GetMapping("/by-product/{productId}")
    public ApiResponse<List<ProductVariantResponse>> getVariantsByProduct(@PathVariable String productId) {
        List<ProductVariant> variants = productVariantService.findByProduct(productId);
        List<ProductVariantResponse> responses = variants.stream()
                .map(productMapper::toProductVariantResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .message("ProductVariants retrieved")
                .body(responses)
                .build();
    }
}
