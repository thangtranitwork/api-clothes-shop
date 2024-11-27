package com.clothes.noc.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCartItemQuantityRequest {
    private String variantId;
    //can delete cart item
    @Min(value = 0, message = "INVALID_QUANTITY")
    @Max(value = 10, message = "INVALID_QUANTITY")
    private int quantity;
}
