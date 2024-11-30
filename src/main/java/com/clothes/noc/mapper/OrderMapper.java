package com.clothes.noc.mapper;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.response.OrderItemResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);
    Order toOrder(OrderRequest request);
    @Mapping(target = "product", source = "productVariant.product")
    @Mapping(target = "variant", source = "productVariant")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
