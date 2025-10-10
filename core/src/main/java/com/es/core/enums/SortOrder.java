package com.es.core.enums;

import java.util.Arrays;

public enum SortOrder {
    ASC("asc"), DESC("desc");

    private final String sortOrder;

    SortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public static SortOrder getEnum(String sortOrder) {
        return Arrays.stream(SortOrder.values())
                .filter(order -> order.sortOrder.equals(sortOrder))
                .findFirst()
                .orElse(ASC);
    }

    public String getSortOrder() {
        return sortOrder;
    }
}
