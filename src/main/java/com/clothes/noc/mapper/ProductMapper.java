package com.clothes.noc.mapper;

import com.clothes.noc.dto.response.*;
import com.clothes.noc.entity.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    ProductFullResponse toProductFullResponse(Product product);
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    SizeResponse toSizeResponse(Size size);
    ColorResponse toColorResponse(Color color);
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
}
