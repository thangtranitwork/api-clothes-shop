package com.clothes.admin.service.impl;

import com.clothes.admin.service.ProductVariantAdminService;
import com.clothes.admin.service.SizeAdminService;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.entity.Size;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SizeAdminServiceImpl implements SizeAdminService {
    private final SizeRepository sizeRepository;
    private final ProductVariantAdminService productVariantAdminService;

    @Autowired
    public SizeAdminServiceImpl(SizeRepository sizeRepository, ProductVariantAdminService productVariantAdminService) {
        this.sizeRepository = sizeRepository;
        this.productVariantAdminService = productVariantAdminService;
    }

    @Override
    public List<Size> findAll() {
        return sizeRepository.findAll();
    }

    @Override
    public Size findById(String id) {
        return sizeRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.SIZE_NOT_FOUND));
    }

    @Override
    public Size save(Size size) {
        Optional<Size> existedRecord = sizeRepository.findById(size.getId());
        if (existedRecord.isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_SIZE);
        }
        return sizeRepository.save(size);
    }

    @Override
    public Size update(String id, Size updatedSize) {
        Size existingSize = sizeRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.SIZE_NOT_FOUND));
        updatedSize.setId(existingSize.getId());
        return sizeRepository.save(updatedSize);
    }

    @Override
    public void deleteById(String id) {
        if (!sizeRepository.existsById(id)) {
            throw new AppException(ErrorCode.SIZE_NOT_FOUND);
        }
        try {
            sizeRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SIZE_DELETE_FAILED);
        }
    }

    @Override
    public Size getSizeByProductVariantId(String productVariantId) {
        ProductVariant productVariant = productVariantAdminService.findById(productVariantId);
        return productVariant.getSize();
    }
}
