package com.es.core.util;

public class LogMessageCreator {
    private LogMessageCreator() {}

    public static String createExceptionMessage(Throwable throwable) {
        if (throwable == null) {
            return "Exception is null";
        }
        String className = throwable.getStackTrace().length > 0
                ? throwable.getStackTrace()[0].getClassName() : "Unknown Class";
        return String.format("Exception %s occurred at %s: %s",
                throwable.getClass().getName(), className, throwable.getMessage());
    }
}
