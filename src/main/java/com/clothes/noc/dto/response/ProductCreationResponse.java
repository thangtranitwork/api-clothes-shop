package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreationResponse {
    String id;
    String name;
    int price;
    String description;
    String path;
    String img;
    String hoverImg;
    ProductTypeResponse type;
}