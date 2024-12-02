package com.clothes.admin.mapper;

import com.clothes.admin.dto.OrderAdminResponse;
import com.clothes.noc.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
@Component
public interface OrderAdminMapper {
    OrderAdminResponse toOrderAdminResponse(Order order);
}
