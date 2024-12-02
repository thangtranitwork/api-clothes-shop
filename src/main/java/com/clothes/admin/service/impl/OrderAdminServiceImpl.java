package com.clothes.admin.service.impl;

import com.clothes.admin.repository.OrderAdminRepository;
import com.clothes.admin.service.OrderAdminService;
import com.clothes.noc.entity.Order;
import com.clothes.noc.entity.OrderItem;
import com.clothes.noc.entity.OrderStatus;
import com.clothes.noc.entity.Payment;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderAdminServiceImpl implements OrderAdminService {
    private final OrderAdminRepository orderAdminRepository;

    @Autowired
    public OrderAdminServiceImpl(OrderAdminRepository orderAdminRepository) {
        this.orderAdminRepository = orderAdminRepository;
    }

    @Override
    public List<Order> getAll() {
        return orderAdminRepository.findAll();
    }

    @Override
    public Order findById(String id) {
        return orderAdminRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public void changeOrderStatus(String id, String status) {
        Order existingOrder = orderAdminRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setStatus(OrderStatus.valueOf(status));
        orderAdminRepository.save(existingOrder);
    }

    @Override
    public List<Order> filterByStatus(String status) {
        return orderAdminRepository.findByStatus(OrderStatus.valueOf(status));
    }

    @Override
    public List<Order> filterByPayment(Payment payment) {
        return orderAdminRepository.findByPayment(payment);
    }

    @Override
    public List<Order> filterByOrderTime(LocalDateTime startTime, LocalDateTime endTime) {
        return orderAdminRepository.findByOrderTimeBetween(startTime, endTime);
    }

    @Override
    public double getOrderTotalPrice(Order order) {
        double totalPrice = 0;
        for(OrderItem item : order.getItems())
            totalPrice += (item.getPrice()*item.getQuantity());
        return totalPrice;
    }
}
