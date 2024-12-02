package com.clothes.admin.controller;

import com.clothes.admin.dto.OrderAdminResponse;
import com.clothes.admin.mapper.OrderAdminMapper;
import com.clothes.admin.service.OrderAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/orders")
public class OrderAdminController {

    private final OrderAdminService orderService;
    private final OrderAdminMapper orderMapper;

    @Autowired
    public OrderAdminController(OrderAdminService orderService, OrderAdminMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ApiResponse<List<OrderAdminResponse>> getAllOrders() {
        List<Order> orderList = orderService.getAll();
        List<OrderAdminResponse> orderAdminResponse = orderList.stream()
                .map(order -> {
                    OrderAdminResponse response = orderMapper.toOrderAdminResponse(order);
                    response.setTotalPrice(orderService.getOrderTotalPrice(order));
                    response.setUsername(order.getUser().getFirstname() + " " + order.getUser().getLastname());
                    return response;
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<OrderAdminResponse>>builder()
                .message("Orders retrieved successfully")
                .body(orderAdminResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderAdminResponse> getOrderbyId(@PathVariable String id) {
        Order order = orderService.findById(id);
        OrderAdminResponse orderAdminResponse = orderMapper.toOrderAdminResponse(order);
        orderAdminResponse.setTotalPrice(orderService.getOrderTotalPrice(order));
        orderAdminResponse.setUsername(order.getUser().getFirstname() + " " + order.getUser().getLastname());
        return ApiResponse.<OrderAdminResponse>builder()
                .message("Order retrieved successfully")
                .body(orderAdminResponse)
                .build();
    }

    @PatchMapping("/{id}/update")
    public ApiResponse<Void> changeOrderStatus(@PathVariable String id, @RequestParam String status) {
        orderService.changeOrderStatus(id, status);
        return ApiResponse.<Void>builder()
                .message("Order status updated successfully")
                .build();
    }

}

