package com.clothes.noc.enums;

import java.util.List;

public enum OrderStatus {
    REJECTED(List.of()),
    FAILED(List.of()),
    SUCCESS(List.of()),
    DELIVERY(List.of(SUCCESS, FAILED)),
    PACKING(List.of(DELIVERY)),
    NEW(List.of(PACKING, REJECTED)),
    CANCELLED(List.of(NEW)),
    ;

    private final List<OrderStatus> validNextStatuses;

    OrderStatus(List<OrderStatus> validNextStatuses) {
        this.validNextStatuses = validNextStatuses;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return validNextStatuses.contains(newStatus);
    }
}
