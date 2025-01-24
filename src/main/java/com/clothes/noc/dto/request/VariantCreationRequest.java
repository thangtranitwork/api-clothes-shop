package com.clothes.noc.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class VariantCreationRequest {
    @Min(value = 0, message = "INVALID_VARIANT_QUANTITY")
    Integer quantity;
    String color;
    String size;
    MultipartFile img;
    @Builder.Default
    String existedImageUrl = "";
}
