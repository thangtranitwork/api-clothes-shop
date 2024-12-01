package com.clothes.admin.service;

import com.clothes.noc.entity.Product;

import java.util.List;

public interface ProductAdminService {
    List<Product> findAll();
    Product findById(String id);
    Product save(Product product);
    Product update(String id, Product updatedProduct);
    void deleteById(String id);

    Product getProductByProductVariantId(String productVariantId);
}
