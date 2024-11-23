package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ColorResponse {
    String code;
    String name;
}
