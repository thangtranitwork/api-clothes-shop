package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColorWithQuantityResponse {
    String code;
    String name;
    int quantity;
}
