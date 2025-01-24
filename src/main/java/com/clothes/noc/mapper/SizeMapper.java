package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.SizeCreationRequest;
import com.clothes.noc.dto.response.SizeResponse;
import com.clothes.noc.entity.Size;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    SizeResponse toSizeResponse(Size size);

    Size toSize(SizeCreationRequest request);
}
