package com.clothes.noc.mapper;

import com.clothes.noc.dto.response.ProductVariantResponse;
import com.clothes.noc.entity.ProductVariant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
}
