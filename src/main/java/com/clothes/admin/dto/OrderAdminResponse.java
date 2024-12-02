package com.clothes.admin.dto;

import com.clothes.noc.dto.response.PaymentResponse;
import com.clothes.noc.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderAdminResponse {
    String id;
    LocalDateTime orderTime;
    String phoneNumber;
    String address;
    String note;
    OrderStatus status;
    double totalPrice;
    PaymentResponse payment;
    String payUrl;
    String username;
}
