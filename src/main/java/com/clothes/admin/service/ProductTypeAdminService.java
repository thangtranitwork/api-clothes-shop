package com.clothes.admin.service;

import com.clothes.noc.entity.ProductType;

import java.util.List;

public interface ProductTypeAdminService {
    List<ProductType> findAll();
    ProductType findById(String id);
    ProductType save(ProductType productType);
    ProductType update(String id, ProductType updatedType);
    void deleteById(String id);
}
