package com.clothes.noc.dto.response;

import com.clothes.noc.entity.OrderItem;
import com.clothes.noc.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    String id;
    Date orderTime;
    String phoneNumber;
    String address;
    String note;
    OrderStatus status;
    List<OrderItemResponse> items;
    PaymentResponse payment;
    String payURL;
}
