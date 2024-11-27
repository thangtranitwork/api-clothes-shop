package com.clothes.noc.mapper;

import com.clothes.noc.dto.response.CartItemResponse;
import com.clothes.noc.dto.response.CartResponse;
import com.clothes.noc.entity.Cart;
import com.clothes.noc.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {
    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "product", source = "productVariant.product")
    @Mapping(target = "variant", source = "productVariant")
    CartItemResponse toCartItemResponse(CartItem cartItem);
}