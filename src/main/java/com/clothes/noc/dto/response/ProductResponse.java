package com.clothes.noc.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class ProductResponse {
    String id;
    String name;
    String price;
    String img;
    String hoverImg;
    String path;
    ProductTypeResponse type;
    List<ColorResponse> colors;
    List<SizeResponse> sizes;
}
