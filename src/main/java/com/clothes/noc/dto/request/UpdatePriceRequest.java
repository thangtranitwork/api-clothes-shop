package com.clothes.noc.dto.request;


import jakarta.validation.constraints.Min;

public record UpdatePriceRequest(
        @Min(value = 1, message = "INVALID_PRICE")
        int price
) {
}
