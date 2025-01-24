package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    ProductResponse product;
    ProductVariantResponse variant;
    int quantity;
    int price;
}
