package com.clothes.noc.dto.response;

import com.clothes.noc.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderWithUserResponse {
    String id;
    LocalDateTime orderTime;
    String phoneNumber;
    String address;
    String note;
    OrderStatus status;
    List<OrderItemResponse> items;
    PaymentResponse payment;
    UserProfileResponse user;
}
