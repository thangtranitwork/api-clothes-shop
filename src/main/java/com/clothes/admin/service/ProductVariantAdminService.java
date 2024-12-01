package com.clothes.admin.service;

import com.clothes.noc.entity.ProductVariant;

import java.util.List;

public interface ProductVariantAdminService {
    List<ProductVariant> findAll();
    ProductVariant findById(String id);
    ProductVariant save(ProductVariant productVariant);
    ProductVariant update(String id, ProductVariant updatedVariant);
    void deleteById(String id);
    List<ProductVariant> findByProduct(String productId);
    List<ProductVariant> findByColor(String colorCode);
    List<ProductVariant> findBySize(String sizeId);
}
