package com.clothes.admin.service.impl;

import com.clothes.admin.service.OrderItemAdminService;
import com.clothes.noc.entity.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemAdminServiceImpl implements OrderItemAdminService {
    @Override
    public List<OrderItem> findAll() {
        return null;
    }

    @Override
    public OrderItem findById(String id) {
        return null;
    }


    @Override
    public void deleteById(String id) {

    }
}
