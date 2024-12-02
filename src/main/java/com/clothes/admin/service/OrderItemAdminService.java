package com.clothes.admin.service;

import com.clothes.noc.entity.OrderItem;

import java.util.List;

public interface OrderItemAdminService {
    List<OrderItem> findByOrderId(String orderId);
}
