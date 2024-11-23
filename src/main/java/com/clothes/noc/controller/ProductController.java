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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {
    final ProductService productService;

    @GetMapping("/search")
    ApiResponse<Page<ProductResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String subtype,
            @RequestParam(required = false) List<String> colors,
            @RequestParam(required = false) List<String> sizes,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int pageSize
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .type(type)
                .subtype(subtype)
                .colors(colors)
                .sizes(sizes)
                .build();

        return ApiResponse.<Page<ProductResponse>>builder()
                .body(productService.search(request, page, pageSize))
                .build();
    }

    @GetMapping("/type/{type}")
    ApiResponse<ColorsAndSizeResponse> getColorsAndSizesOfAType(@PathVariable String type) {
        return ApiResponse.<ColorsAndSizeResponse>builder()
                .body(productService.getColorsAndSizesOfAType(type))
                .build();
    }

    @GetMapping("/subtype/{subtype}")
    ApiResponse<ColorsAndSizeResponse> getColorsAndSizesOfASubtype(@PathVariable String subtype) {
        return ApiResponse.<ColorsAndSizeResponse>builder()
                .body(productService.getColorsAndSizesOfASubtype(subtype))
                .build();
    }

    @GetMapping("{id}")
    ApiResponse<ProductWithVariantResponse> getProduct(@PathVariable String id) {
        return ApiResponse.<ProductWithVariantResponse>builder()
                .body(productService.get(id))
                .build();
    }
}
