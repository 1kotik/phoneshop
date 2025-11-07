package com.es.core.enums;

import java.util.Arrays;

public enum OrderStatus {
    NEW("New"),
    DELIVERED("Delivered"),
    REJECTED("Rejected");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus getOrderStatus(String value) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getValue().equals(value))
                .findFirst()
                .orElse(NEW);
    }
}
