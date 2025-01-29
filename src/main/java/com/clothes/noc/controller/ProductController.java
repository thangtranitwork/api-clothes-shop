package com.clothes.noc.controller;

import com.clothes.noc.dto.request.SearchProductRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ColorsAndSizeResponse;
import com.clothes.noc.dto.response.ProductResponse;
import com.clothes.noc.dto.response.ProductWithVariantResponse;
import com.clothes.noc.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {
    final ProductService productService;

    @GetMapping("/search")
    ApiResponse<Page<ProductResponse>> search(
            SearchProductRequest request,
            Pageable pageable
    ) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .body(productService.search(request, pageable))
                .build();
    }

    @GetMapping("/colors_sizes")
    ApiResponse<ColorsAndSizeResponse> getColorsAndSizes() {
        return ApiResponse.<ColorsAndSizeResponse>builder()
                .body(productService.getColorsAndSizes())
                .build();
    }

    @GetMapping("/colors_sizes/type/{type}")
    ApiResponse<ColorsAndSizeResponse> getColorsAndSizesOfAType(@PathVariable String type) {
        return ApiResponse.<ColorsAndSizeResponse>builder()
                .body(productService.getColorsAndSizesOfAType(type))
                .build();
    }

    @GetMapping("/colors_sizes/subtype/{subtype}")
    ApiResponse<ColorsAndSizeResponse> getColorsAndSizesOfASubtype(@PathVariable String subtype) {
        return ApiResponse.<ColorsAndSizeResponse>builder()
                .body(productService.getColorsAndSizesOfASubtype(subtype))
                .build();
    }

    @GetMapping("{path}")
    ApiResponse<ProductWithVariantResponse> getProduct(@PathVariable String path) {
        return ApiResponse.<ProductWithVariantResponse>builder()
                .body(productService.getByPath(path))
                .build();
    }

    @GetMapping("/hottest")
    ApiResponse<Page<ProductResponse>> getHottestProduct(Pageable pageable) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .body(productService.getHotProducts(pageable))
                .build();
    }

    @GetMapping("/latest")
    ApiResponse<Page<ProductResponse>> getLatestProduct(Pageable pageable) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .body(productService.getHotProducts(pageable))
                .build();
    }
}
