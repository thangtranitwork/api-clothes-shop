package com.clothes.admin.repository;

import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.OrderStatus;
import com.clothes.noc.entity.Payment;
import com.clothes.noc.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderAdminRepository extends OrderRepository {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByPayment(Payment payment);
    List<Order> findByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
