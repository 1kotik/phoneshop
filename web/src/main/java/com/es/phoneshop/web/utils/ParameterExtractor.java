package com.es.phoneshop.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParameterExtractor {
    private ParameterExtractor() {}

    public static Long extractItemIdFromBindingResultField(String fieldName) {
        Pattern pattern = Pattern.compile("items\\[(\\d+)]");
        Matcher matcher = pattern.matcher(fieldName);
        return matcher.find() ? Long.valueOf(matcher.group(1)) : null;
    }
}
