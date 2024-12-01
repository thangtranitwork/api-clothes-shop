package com.clothes.admin.service.impl;

import com.clothes.admin.repository.ProductVariantAdminRepository;
import com.clothes.admin.service.ProductVariantAdminService;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductVariantAdminServiceImpl implements ProductVariantAdminService {
    private final ProductVariantAdminRepository productVariantRepository;

    @Autowired
    public ProductVariantAdminServiceImpl(ProductVariantAdminRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public List<ProductVariant> findAll() {
        return productVariantRepository.findAll();
    }

    @Override
    public ProductVariant findById(String id) {
        return productVariantRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
    }

    @Override
    public ProductVariant save(ProductVariant productVariant) {
        if (productVariant.getId() == null || productVariant.getId().isEmpty()) {
            String newId;
            Optional<ProductVariant> existedRecord;

            do {
                newId = UUID.randomUUID().toString();
                existedRecord = productVariantRepository.findById(newId);
            } while (existedRecord.isPresent());

            productVariant.setId(newId);
        }

        return productVariantRepository.save(productVariant);
    }

    @Override
    public ProductVariant update(String id, ProductVariant updatedVariant) {
        System.out.println(updatedVariant);
        ProductVariant existingVariant = productVariantRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        updatedVariant.setId(existingVariant.getId());
        return productVariantRepository.save(updatedVariant);
    }

    @Override
    public void deleteById(String id) {
        if (!productVariantRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }
        try {
            productVariantRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_DELETE_FAILED);
        }
    }

    @Override
    public List<ProductVariant> findByProduct(String productId) {
        return productVariantRepository.findByProductIdNative(productId);
    }

    @Override
    public List<ProductVariant> findByColor(String colorCode) {
        return productVariantRepository.findByColorCodeNative(colorCode);
    }

    @Override
    public List<ProductVariant> findBySize(String sizeId) {
        return productVariantRepository.findBySizeIdNative(sizeId);
    }

}
