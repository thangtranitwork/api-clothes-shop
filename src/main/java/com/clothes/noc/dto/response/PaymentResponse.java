package com.clothes.noc.dto.response;

import com.clothes.noc.entity.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PaymentResponse {
    String id;
    PaymentType type;
    Date payTime;
}
