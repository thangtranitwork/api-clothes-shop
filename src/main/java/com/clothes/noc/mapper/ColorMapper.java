package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.ColorRequest;
import com.clothes.noc.dto.response.ColorResponse;
import com.clothes.noc.entity.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    ColorResponse toColorResponse(Color color);
    Color toColor(ColorRequest request);
}
