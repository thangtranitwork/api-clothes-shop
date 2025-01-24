package com.clothes.noc.dto.response;

import com.clothes.noc.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    String id;
    LocalDateTime orderTime;
    String phoneNumber;
    String address;
    String note;
    OrderStatus status;
    List<OrderItemResponse> items;
    PaymentResponse payment;
    String payUrl;
}
