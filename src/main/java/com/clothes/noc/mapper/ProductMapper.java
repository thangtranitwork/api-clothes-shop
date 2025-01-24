package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.ProductCreationRequest;
import com.clothes.noc.dto.response.AdminProductResponse;
import com.clothes.noc.dto.response.ProductCreationResponse;
import com.clothes.noc.dto.response.ProductFullResponse;
import com.clothes.noc.dto.response.ProductResponse;
import com.clothes.noc.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColorMapper.class, VariantMapper.class, SizeMapper.class, ProductTypeMapper.class})
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    ProductFullResponse toProductFullResponse(Product product);
    @Mapping(target = "type", ignore = true)
    Product toProduct(ProductCreationRequest productCreationRequest);
    ProductCreationResponse toProductCreationResponse(Product product);
    @Mapping(target = "variants", source = "productVariants")
    AdminProductResponse toAdminProductResponse(Product product);
}
