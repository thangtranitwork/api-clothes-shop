package com.clothes.noc.controller;

import com.clothes.noc.dto.request.OrderRequest;
import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.CreateOrderResponse;
import com.clothes.noc.dto.response.OrderResponse;
import com.clothes.noc.entity.OrderStatus;
import com.clothes.noc.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderController {
    final OrderService orderService;


    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrder(@PathVariable String id) {
        return ApiResponse.<OrderResponse>builder()
                .body(orderService.getOrder(id))
                .build();
    }

    @GetMapping("/history")
    ApiResponse<Page<OrderResponse>> getHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) String status
            ) {

        SearchOrderRequest request = SearchOrderRequest.builder()
                .id(id)
                .from(from)
                .to(to)
                .build();
        try{
            OrderStatus orderStatus = OrderStatus.valueOf(status);
            request.setStatus(orderStatus);
        }catch (IllegalArgumentException e){

        } catch (NullPointerException e){

        }
        return ApiResponse.<Page<OrderResponse>>builder()
                .body(orderService.getHistory(request, page - 1, pageSize))
                .build();
    }

    @GetMapping("/payment-return")
    void paymentReturn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        orderService.handlePayment(req, resp);
    }

    @GetMapping("/pay-again/{id}")
    ApiResponse<String> payAgainOrder(@PathVariable String id, HttpServletRequest request) {
        return ApiResponse.<String>builder()
                .body(orderService.payAgain(id, request))
                .build();
    }

    @PostMapping("/new")
    ApiResponse<CreateOrderResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest) {
        return ApiResponse.<CreateOrderResponse>builder()
                .body(orderService.createOrder(orderRequest, httpServletRequest))
                .build();
    }

    @PatchMapping("/cancel/{id}")
    ApiResponse<OrderResponse> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ApiResponse.<OrderResponse>builder().build();
    }

}
