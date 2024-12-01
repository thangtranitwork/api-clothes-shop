package com.clothes.noc.mapper;

import com.clothes.noc.dto.response.*;
import com.clothes.noc.entity.*;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    ProductFullResponse toProductFullResponse(Product product);
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    SizeResponse toSizeResponse(Size size);
    ColorResponse toColorResponse(Color color);
    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);
}
