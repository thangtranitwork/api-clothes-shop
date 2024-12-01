package com.clothes.admin.controller;

import com.clothes.admin.service.ProductAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ColorResponse;
import com.clothes.noc.dto.response.ProductFullResponse;
import com.clothes.noc.dto.response.ProductFullResponse;
import com.clothes.noc.entity.Color;
import com.clothes.noc.entity.Product;
import com.clothes.noc.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {
    private final ProductAdminService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductAdminController(ProductAdminService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ApiResponse<ProductFullResponse> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ApiResponse.<ProductFullResponse>builder()
                .message("Product created successfully")
                .body(productMapper.toProductFullResponse(savedProduct))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductFullResponse> getProduct(@PathVariable String id) {
        Product product = productService.findById(id);
        return ApiResponse.<ProductFullResponse>builder()
                .message("Product found")
                .body(productMapper.toProductFullResponse(product))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductFullResponse>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductFullResponse> productResponses = products.stream()
                .map(productMapper::toProductFullResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ProductFullResponse>>builder()
                .message("Products retrieved")
                .body(productResponses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductFullResponse> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Product updatedProduct = productService.update(id, product);
        return ApiResponse.<ProductFullResponse>builder()
                .message("Product updated")
                .body(productMapper.toProductFullResponse(updatedProduct))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct(@PathVariable String id) {
        productService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Product deleted successfully")
                .body(null)
                .build();
    }

    @GetMapping("/variant/{id}")
    public ApiResponse<ProductFullResponse> getColorByProductVariantId(@PathVariable String id) {
        Product product = productService.getProductByProductVariantId(id);
        return ApiResponse.<ProductFullResponse>builder()
                .message("Color updated")
                .body(productMapper.toProductFullResponse(product))
                .build();
    }
}

