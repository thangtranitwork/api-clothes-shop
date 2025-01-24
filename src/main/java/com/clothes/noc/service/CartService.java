package com.clothes.noc.service;

import com.clothes.noc.dto.request.VariantInCartRequest;
import com.clothes.noc.dto.response.CartResponse;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.CartMapper;
import com.clothes.noc.repository.CartItemRepository;
import com.clothes.noc.repository.CartRepository;
import com.clothes.noc.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    @NonFinal
    private static final int MAX_QUANTITY = 10;

    public void add(VariantInCartRequest request) {
        // Tìm ProductVariant
        Integer quantity = productVariantRepository.getQuantityById(request.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXIST));
        if (request.getQuantity() > quantity) {
            throw new AppException(ErrorCode.QUANTITY_OF_PRODUCT_NOT_MEETING_REQUEST);
        }
        String userId = userService.getCurrentUserId();
        cartItemRepository.getQuantityByUserIdAndVariantId(userId, request.getVariantId())
                .ifPresentOrElse(
                        currentQuantity ->
                                cartItemRepository.updateQuantity(
                                        userId,
                                        request.getVariantId(),
                                        Math.min(currentQuantity + request.getQuantity(), MAX_QUANTITY)),
                        () ->
                                cartItemRepository.addCartItem(
                                        UUID.randomUUID().toString(),
                                        cartRepository.findByUserId(userId).getId(),
                                        request.getVariantId(),
                                        request.getQuantity()));
    }

    public CartResponse getCart() {
        return cartMapper.toCartResponse(cartRepository.findByUserId(userService.getCurrentUser().getId()));
    }

    public void updateQuantity(VariantInCartRequest request) {
        if (cartItemRepository.updateQuantity(userService.getCurrentUserId(), request.getVariantId(), request.getQuantity()) == 0) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
        }
    }

    public void removeItem(String variantId) {
        if (cartItemRepository.removeItem(userService.getCurrentUserId(), variantId) == 0) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
        }
    }
    public void clearCart() {
        cartItemRepository.removeAllItem(userService.getCurrentUserId());
    }

}

