package com.es.core.util;

public class LogMessageCreator {
    private LogMessageCreator() {}

    public static String createExceptionMessage(Throwable throwable) {
        return String.format("Exception %s occurred at %s: %s",
                throwable.getClass().getName(), throwable.getStackTrace()[0].getClassName(), throwable.getMessage());
    }
}
