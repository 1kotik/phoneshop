package com.es.core.enums;

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
}
