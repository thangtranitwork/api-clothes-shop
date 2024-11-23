package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColorsAndSizeResponse {
    List<ColorWithQuantityResponse> colors;
    List<SizeWithQuantityResponse> sizes;
}
