package com.clothes.noc.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductTypeRequest {
    String type;
    String subtype;
}
