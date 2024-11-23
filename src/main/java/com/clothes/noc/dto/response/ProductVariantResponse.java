package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariantResponse {
    String id;
    String img;
    int quantity;
    SizeResponse size;
    ColorResponse color;
}
