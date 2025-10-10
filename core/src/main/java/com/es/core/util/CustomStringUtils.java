package com.es.core.util;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomStringUtils {
    private CustomStringUtils() {
    }

    public static String getEnumerationOfIds(Collection<Long> idCollection) {
        return idCollection.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
