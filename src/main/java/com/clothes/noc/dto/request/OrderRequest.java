package com.clothes.noc.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequest {
    String phoneNumber;
    String address;
    String note;
    String paymentMethod;
}
