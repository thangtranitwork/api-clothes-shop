package com.clothes.admin.service.impl;

import com.clothes.admin.service.ColorAdminService;
import com.clothes.admin.service.ProductVariantAdminService;
import com.clothes.noc.entity.Color;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorAdminServiceImpl implements ColorAdminService {
    private final ColorRepository colorRepository;
    private final ProductVariantAdminService productVariantAdminService;

    @Autowired
    public ColorAdminServiceImpl(ColorRepository colorRepository, ProductVariantAdminService productVariantAdminService) {
        this.colorRepository = colorRepository;
        this.productVariantAdminService = productVariantAdminService;
    }

    @Override
    public List<Color> findAll() {
        return colorRepository.findAll();
    }

    @Override
    public Color findByCode(String code) {
        return colorRepository.findById(code).orElseThrow(() ->
                new AppException(ErrorCode.COLOR_NOT_FOUND));
    }

    @Override
    public Color save(Color color) {
        Optional<Color> existedRecord = colorRepository.findById(color.getCode());
        if (existedRecord.isPresent()) {
            throw new AppException(ErrorCode.DUPLICATE_COLOR);
        }
        return colorRepository.save(color);
    }

    @Override
    public Color update(String code, Color updatedColor) {
        Color existingColor = colorRepository.findById(code).orElseThrow(() ->
                new AppException(ErrorCode.COLOR_NOT_FOUND));
        updatedColor.setCode(existingColor.getCode());
        return colorRepository.save(updatedColor);
    }

    @Override
    public void deleteByCode(String code) {
        if (!colorRepository.existsById(code)) {
            throw new AppException(ErrorCode.COLOR_NOT_FOUND);
        }
        try {
            colorRepository.deleteById(code);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.COLOR_DELETE_FAILED);
        }
    }

    @Override
    public Color getColorByProductVariantId(String productVariantId) {
        ProductVariant productVariant = productVariantAdminService.findById(productVariantId);
        return productVariant.getColor();
    }
}
