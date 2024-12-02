package com.clothes.admin.controller;

import com.clothes.admin.service.OrderItemAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.OrderItemResponse;
import com.clothes.noc.entity.OrderItem;
import com.clothes.noc.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/order-items")
public class OrderItemAdminController {
    private final OrderItemAdminService orderItemService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderItemAdminController(OrderItemAdminService orderItemService, OrderMapper orderMapper) {
        this.orderItemService = orderItemService;
        this.orderMapper = orderMapper;
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<List<OrderItemResponse>> getOrderItemsByOrderId(@PathVariable String orderId) {
        List<OrderItem> orderItems = orderItemService.findByOrderId(orderId);
        List<OrderItemResponse> orderItemResponses = orderItems.stream()
                .map(orderMapper::toOrderItemResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<OrderItemResponse>>builder()
                .message("Order items retrieved successfully")
                .body(orderItemResponses)
                .build();
    }
}
