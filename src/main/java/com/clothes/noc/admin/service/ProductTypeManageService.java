package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.ProductTypeRequest;
import com.clothes.noc.dto.response.ProductTypeResponse;
import com.clothes.noc.entity.ProductType;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.ProductTypeMapper;
import com.clothes.noc.repository.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeManageService {
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;

    public ProductType get(String id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    public List<ProductTypeResponse> getAll() {
        return productTypeRepository.findAll().stream()
                .map(productTypeMapper::toProductTypeResponse)
                .toList();
    }

    public ProductTypeResponse add(ProductTypeRequest productTypeRequest) {
        if (productTypeRepository.existsBySubtype(productTypeRequest.getSubtype())) {
            throw new AppException(ErrorCode.PRODUCT_SUBTYPE_TYPE_HAS_ALREADY_EXISTS);
        }
        return productTypeMapper.toProductTypeResponse(productTypeRepository.save(productTypeMapper.toProductType(productTypeRequest)));
    }

    public void delete(String id) {
        if (productTypeRepository.countProductById(id) > 0) {
            throw new AppException(ErrorCode.PRODUCT_TYPE_DELETE_FAILED);
        }
        productTypeRepository.deleteById(id);
    }
}
