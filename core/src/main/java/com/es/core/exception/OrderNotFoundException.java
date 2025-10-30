package com.es.core.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    private UUID orderId;
    public OrderNotFoundException(UUID orderId) {
        super("Sorry. Your order has not been found.");
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
