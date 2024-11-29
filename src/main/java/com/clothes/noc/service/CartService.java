package com.clothes.noc.service;

import com.clothes.noc.dto.request.VariantInCartRequest;
import com.clothes.noc.dto.response.CartResponse;
import com.clothes.noc.entity.Cart;
import com.clothes.noc.entity.CartItem;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.entity.User;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.CartMapper;
import com.clothes.noc.repository.CartItemRepository;
import com.clothes.noc.repository.CartRepository;
import com.clothes.noc.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserService userService;
    private final CartMapper cartMapper;
    @NonFinal
    private final int MAX_QUANTITY = 10;

    public void add(VariantInCartRequest request) {
        // Tìm ProductVariant
        ProductVariant productVariant = productVariantRepository.findById(request.getVariantId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXIST));

        if (request.getQuantity() > productVariant.getQuantity()) {
            throw new AppException(ErrorCode.QUANTITY_OF_PRODUCT_NOT_MEETING_REQUEST);
        }

        User user = userService.getCurrentUser();
        user.getCart().getItems().stream()
                .filter(item ->
                        item.getProductVariant().getId()
                                .equals(productVariant.getId()))
                .findFirst()
                .ifPresentOrElse(
                        (item) -> {
                            int newQuantity = Math.min(item.getQuantity() + request.getQuantity(), MAX_QUANTITY);
                            item.setQuantity(newQuantity);
                        },
                        () -> {
                            var cartItem = CartItem.builder()
                                    .productVariant(productVariant)
                                    .quantity(request.getQuantity())
                                    .cart(user.getCart())
                                    .build();
                            user.getCart().getItems().add(cartItem);
                        }
                );

        cartRepository.save(user.getCart());
    }

    public CartResponse getCart() {
        return cartMapper.toCartResponse(userService.getCurrentUser().getCart());
    }

    public void updateQuantity(VariantInCartRequest request) {
        Cart cart = userService.getCurrentUser().getCart();

        cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(request.getVariantId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(Math.min(request.getQuantity(), MAX_QUANTITY)),
                        () -> {
                            throw new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
                        }
                );

        cartRepository.save(cart);
    }

    public void removeItem(String variantId) {
        Cart cart = userService.getCurrentUser().getCart();
        cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variantId))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            cart.getItems().remove(item);
                            cartItemRepository.delete(item);
                        },
                        () -> {
                            throw new AppException(ErrorCode.CART_ITEM_NOT_EXIST);
                        }
                );
        cartRepository.save(cart);
    }
    public void clearCart() {
        Cart cart = userService.getCurrentUser().getCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}

