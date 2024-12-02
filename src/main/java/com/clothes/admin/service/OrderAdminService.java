package com.clothes.admin.service;

import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderAdminService {
    List<Order> getAll();

    Order findById(String id);
    void changeOrderStatus(String id, String status);
    List<Order> filterByStatus(String status);
    List<Order> filterByPayment(Payment payment);
    List<Order> filterByOrderTime(LocalDateTime startTime, LocalDateTime endTime);

    //========================================================
    double getOrderTotalPrice(Order order);
}
