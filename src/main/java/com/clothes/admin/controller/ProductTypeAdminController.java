package com.clothes.admin.controller;

import com.clothes.admin.service.ProductTypeAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ProductTypeResponse;
import com.clothes.noc.entity.ProductType;
import com.clothes.noc.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/product-types")
public class ProductTypeAdminController {
    private final ProductTypeAdminService productTypeService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductTypeAdminController(ProductTypeAdminService productTypeService, ProductMapper productMapper) {
        this.productTypeService = productTypeService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ApiResponse<ProductTypeResponse> createProductType(@RequestBody ProductType productType) {
        ProductType savedProductType = productTypeService.save(productType);
        return ApiResponse.<ProductTypeResponse>builder()
                .message("Product Type created successfully")
                .body(productMapper.toProductTypeResponse(savedProductType))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductTypeResponse> getProductType(@PathVariable String id) {
        ProductType productType = productTypeService.findById(id);
        return ApiResponse.<ProductTypeResponse>builder()
                .message("Product Type found")
                .body(productMapper.toProductTypeResponse(productType))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductTypeResponse>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.findAll();
        List<ProductTypeResponse> responses = productTypes.stream()
                .map(productMapper::toProductTypeResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ProductTypeResponse>>builder()
                .message("Product Types retrieved")
                .body(responses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductTypeResponse> updateProductType(@PathVariable String id, @RequestBody ProductType productType) {
        ProductType updatedProductType = productTypeService.update(id, productType);
        return ApiResponse.<ProductTypeResponse>builder()
                .message("Product Type updated")
                .body(productMapper.toProductTypeResponse(updatedProductType))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductType(@PathVariable String id) {
        productTypeService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Product Type deleted successfully")
                .body(null)
                .build();
    }
}