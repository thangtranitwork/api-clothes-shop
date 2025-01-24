package com.clothes.noc.admin.controller;

import com.clothes.noc.admin.service.OrderManageService;
import com.clothes.noc.dto.request.SearchOrderRequest;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.OrderWithUserResponse;
import com.clothes.noc.dto.response.SummaryResponse;
import com.clothes.noc.enums.OrderStatus;
import com.clothes.noc.enums.SummaryType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PreAuthorize(value = "hasAnyAuthority('ADMIN')")
public class OrderManageController {
    final OrderManageService orderManageService;

    @GetMapping("/search")
    ApiResponse<Page<OrderWithUserResponse>> search(SearchOrderRequest request, Pageable pageable) {
        return ApiResponse.<Page<OrderWithUserResponse>>builder()
                .body(orderManageService.getAll(request, pageable))
                .build();
    }

    @GetMapping("/summary")
    ApiResponse<SummaryResponse> generateSummary(@RequestParam(defaultValue = "DAY") SummaryType type, @RequestParam(defaultValue = "3") int num) {
        return ApiResponse.<SummaryResponse>builder()
                .body(orderManageService.generateSummary(type, num))
                .build();
    }

    @GetMapping("/summary-by-category")
    ApiResponse<SummaryResponse> generateSummaryByCategory(){
        return ApiResponse.<SummaryResponse>builder()
                .body(orderManageService.generateSummaryByCategory())
                .build();
    }
    
    @PatchMapping("/update-status")
    ApiResponse<Void> updateStatus(String id, OrderStatus status) {
        orderManageService.updateOrderStatus(id, status);
        return new ApiResponse<>();
    }

}

