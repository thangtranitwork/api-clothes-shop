package com.clothes.admin.service.impl;

import com.clothes.admin.service.OrderAdminService;
import com.clothes.noc.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderAdminServiceImpl implements OrderAdminService {
    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order findById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
