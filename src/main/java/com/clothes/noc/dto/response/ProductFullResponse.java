package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductFullResponse {
    String id;
    String name;
    String description;
    int price;
    String img;
    String hoverImg;
    String path;
    ProductTypeResponse type;
    List<ColorResponse> colors;
    List<SizeResponse> sizes;
}
