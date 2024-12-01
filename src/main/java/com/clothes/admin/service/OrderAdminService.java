package com.clothes.admin.service;

import com.clothes.noc.entity.Order;

import java.util.List;

public interface OrderAdminService {
    List<Order> findAll();
    Order findById(String id);
    void deleteById(String id);
}
