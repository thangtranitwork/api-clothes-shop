package com.clothes.admin.service.impl;

import com.clothes.admin.service.ProductAdminService;
import com.clothes.admin.service.ProductVariantAdminService;
import com.clothes.noc.entity.Product;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductAdminServiceImpl implements ProductAdminService {
    private final ProductRepository productRepository;
    private final ProductVariantAdminService productVariantAdminService;


    @Autowired
    public ProductAdminServiceImpl(ProductRepository productRepository, ProductVariantAdminService productVariantAdminService) {
        this.productRepository = productRepository;
        this.productVariantAdminService = productVariantAdminService;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            String newId;
            Optional<Product> existedRecord;

            do {
                newId = UUID.randomUUID().toString();
                existedRecord = productRepository.findById(newId);
            } while (existedRecord.isPresent());

            product.setId(newId);
        }

        return productRepository.save(product);
    }

    @Override
    public Product update(String id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        updatedProduct.setId(existingProduct.getId());
        return productRepository.save(updatedProduct);
    }

    @Override
    public void deleteById(String id) {
        if (!productRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.PRODUCT_DELETE_FAILED);
        }
    }

    @Override
    public Product getProductByProductVariantId(String productVariantId) {
        ProductVariant productVariant = productVariantAdminService.findById(productVariantId);
        return productVariant.getProduct();
    }
}


