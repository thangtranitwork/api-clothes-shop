package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.ProductTypeRequest;
import com.clothes.noc.dto.response.ProductTypeResponse;
import com.clothes.noc.entity.ProductType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    ProductType toProductType(ProductTypeRequest productTypeRequest);
}
