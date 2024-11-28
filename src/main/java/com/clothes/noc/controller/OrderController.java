package com.clothes.noc.controller;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderController {
    final OrderService orderService;

    @GetMapping("/history")
    ApiResponse<Page<OrderResponse>> getHistory(Pageable pageable) {
        return ApiResponse.<Page<OrderResponse>>builder()
                .body(orderService.getHistory(pageable))
                .build();
    }

    @PostMapping("/new")
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ApiResponse.<OrderResponse>builder()
                .body(orderService.createOrder(orderRequest))
                .build();
    }

    @PatchMapping("/cancel/{id}")
    ApiResponse<OrderResponse> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ApiResponse.<OrderResponse>builder().build();
    }
}
