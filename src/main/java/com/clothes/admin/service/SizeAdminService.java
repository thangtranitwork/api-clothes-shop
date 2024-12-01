package com.clothes.admin.service;

import com.clothes.noc.entity.Size;

import java.util.List;

public interface SizeAdminService {
    List<Size> findAll();
    Size findById(String id);
    Size save(Size size);
    Size update(String id, Size updatedSize);
    void deleteById(String id);

    Size getSizeByProductVariantId(String productVariantId);
}
