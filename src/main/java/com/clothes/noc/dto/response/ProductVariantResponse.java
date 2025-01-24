package com.clothes.noc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariantResponse {
    String id;
    String img;
    int quantity;
    SizeResponse size;
    ColorResponse color;
}
