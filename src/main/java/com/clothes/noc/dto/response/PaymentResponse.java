package com.clothes.noc.dto.response;

import com.clothes.noc.enums.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    String id;
    PaymentType type;
    LocalDateTime payTime;
}
