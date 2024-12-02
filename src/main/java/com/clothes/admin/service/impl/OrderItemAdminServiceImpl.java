package com.clothes.admin.service.impl;

import com.clothes.admin.repository.OrderItemAdminRepository;
import com.clothes.admin.service.OrderItemAdminService;
import com.clothes.noc.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemAdminServiceImpl implements OrderItemAdminService {
    private final OrderItemAdminRepository orderItemAdminRepository;

    @Autowired
    public OrderItemAdminServiceImpl(OrderItemAdminRepository orderItemAdminRepository) {
        this.orderItemAdminRepository = orderItemAdminRepository;
    }

    @Override
    public List<OrderItem> findByOrderId(String orderId) {
        return orderItemAdminRepository.findByOrderId(orderId);
    }
}
