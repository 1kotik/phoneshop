package com.es.core.util;

public class LogMessageCreator {
    private LogMessageCreator() {}

    public static String createExceptionMessage(Throwable throwable, Class<?> clazz) {
        return String.format("Exception %s occurred at %s: %s",
                throwable.getClass().getName(), clazz.getName(), throwable.getMessage());
    }
}
