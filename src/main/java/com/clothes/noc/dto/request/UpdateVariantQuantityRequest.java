package com.clothes.noc.dto.request;

import jakarta.validation.constraints.Min;

public record UpdateVariantQuantityRequest(@Min(value = 0, message = "INVALID_VARIANT_QUANTITY") int quantity) {
}
