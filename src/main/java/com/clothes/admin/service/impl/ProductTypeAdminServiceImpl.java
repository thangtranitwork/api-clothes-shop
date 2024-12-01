package com.clothes.admin.service.impl;

import com.clothes.admin.service.ProductTypeAdminService;
import com.clothes.noc.entity.ProductType;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeAdminServiceImpl implements ProductTypeAdminService {
    private final ProductTypeRepository productTypeRepository;

    @Autowired
    public ProductTypeAdminServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public ProductType findById(String id) {
        return productTypeRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    @Override
    public ProductType save(ProductType productType) {
        Optional<ProductType> existedRecord = productTypeRepository.findById(productType.getId());
        if (existedRecord.isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_PRODUCT_TYPE);
        }
        return productTypeRepository.save(productType);
    }

    @Override
    public ProductType update(String id, ProductType updatedType) {
        ProductType existingType = productTypeRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        updatedType.setId(existingType.getId());
        return productTypeRepository.save(updatedType);
    }

    @Override
    public void deleteById(String id) {
        if (!productTypeRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
        }
        try {
            productTypeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_DELETE_FAILED);
        }
    }
}
