package com.es.core.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Sorry. Order has not been found.");
    }
}
