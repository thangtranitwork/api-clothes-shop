package com.clothes.admin.service;

import com.clothes.noc.entity.Color;

import java.util.List;

public interface ColorAdminService {
    List<Color> findAll();
    Color findByCode(String code);
    Color save(Color color);
    Color update(String code, Color updatedColor);
    void deleteByCode(String code);
    Color getColorByProductVariantId(String productVariantId);
}
