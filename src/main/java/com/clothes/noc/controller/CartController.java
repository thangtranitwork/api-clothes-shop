package com.clothes.noc.controller;

import com.clothes.noc.dto.request.VariantInCartRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.CartResponse;
import com.clothes.noc.service.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartController {
    final CartService cartService;

    @PostMapping("/add")
    ApiResponse<Void> addProductToCart(@RequestBody @Valid VariantInCartRequest request) {
        cartService.add(request);
        return new ApiResponse<>();
    }

    @GetMapping("")
    ApiResponse<CartResponse> getCart() {

        return ApiResponse.<CartResponse>builder()
                .body(cartService.getCart())
                .build();
    }

    @PutMapping("/update-quantity")
    ApiResponse<Void> updateQuantity(@RequestBody @Valid VariantInCartRequest request) {
        cartService.updateQuantity(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/remove/{id}")
    ApiResponse<Void> removeProductFromCart(@PathVariable String id) {
        cartService.removeItem(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}
