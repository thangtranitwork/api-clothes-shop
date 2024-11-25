package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductWithVariantResponse {
    ProductFullResponse product;
    List<ProductVariantResponse> variants;
    List<String> imgs;
}
