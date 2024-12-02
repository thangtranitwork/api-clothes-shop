package com.clothes.admin.repository;

import com.clothes.noc.entity.OrderItem;
import com.clothes.noc.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemAdminRepository extends OrderItemRepository {
    List<OrderItem> findByOrderId(String orderId);
}
