package com.es.core.util;

import java.util.Arrays;

public class PhoneQueryUtils {
    private PhoneQueryUtils() {}

    public static long getQueryMatchNumber(String brand, String model, String[] queryParts) {
        String fullPhoneName = String.format("%s %s", brand, model).toLowerCase();
        return Arrays.stream(queryParts)
                .filter(fullPhoneName::contains)
                .count();
    }

    public static String[] getQueryParts(String query) {
        return query != null ? query.trim().replaceAll(" +", " ").toLowerCase().split(" ")
                : new String[0];
    }
}
