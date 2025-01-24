package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.response.OrderItemResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.dto.response.OrderWithUserResponse;
import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, UserMapper.class})
@Component
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
    Order toOrder(OrderRequest request);
    @Mapping(target = "product", source = "productVariant.product")
    @Mapping(target = "variant", source = "productVariant")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
    OrderWithUserResponse toOrderWithUserResponse(Order order);
}
