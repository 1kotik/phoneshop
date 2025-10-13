package com.es.core.enums;

import java.util.Arrays;

public enum SortCriteria {
    BRAND("brand"),
    MODEL("model"),
    DISPLAY_SIZE("displaySizeInches"),
    PRICE("price");
    private final String dbColumnName;

    SortCriteria(String dbColumnName) {
        this.dbColumnName = dbColumnName;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public static SortCriteria getEnum(String dbColumnName) {
        return Arrays.stream(SortCriteria.values())
                .filter(criteria -> criteria.dbColumnName.equals(dbColumnName))
                .findFirst()
                .orElse(BRAND);
    }
}
