package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTypeResponse {
    String id;
    String type;
    String subtype;
}
