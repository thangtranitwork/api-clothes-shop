package com.clothes.noc.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ProductCreationRequest {
    String id;
    @NotBlank
    String name;
    @Min(value = 1, message = "INVALID_PRICE")
    Integer price;
    @NotBlank
    String description;
    @NotBlank
    String path;
    MultipartFile image;
    String existedImageUrl;
    MultipartFile hoverImage;
    String existedHoverImageUrl;
    @NotBlank
    String type;
}
