package com.clothes.noc.dto.request;

import com.clothes.noc.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class SearchOrderRequest {
    String id;
    LocalDateTime from;
    LocalDateTime to;
    OrderStatus status;
}
