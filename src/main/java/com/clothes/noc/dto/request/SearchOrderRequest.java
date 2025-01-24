package com.clothes.noc.dto.request;

import com.clothes.noc.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class SearchOrderRequest {
    String id;
    String userId;
    LocalDateTime from;
    LocalDateTime to;
    String status;
    OrderStatus orderStatus;
}
