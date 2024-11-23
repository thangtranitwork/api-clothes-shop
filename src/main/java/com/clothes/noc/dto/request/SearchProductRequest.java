package com.clothes.noc.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchProductRequest {
    String name;
    Double minPrice;
    Double maxPrice;
    String type;
    String subtype;
    List<String> colors;
    List<String> sizes;
}
