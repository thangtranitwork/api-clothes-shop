package com.clothes.noc.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    String id;
    String name;
    int price;
    String img;
    String hoverImg;
    String path;
    ProductTypeResponse type;
    List<ColorResponse> colors;
    List<SizeResponse> sizes;
}
