package com.es.core.exception;

public class OrderHasAlreadyBeenPlacedException extends RuntimeException {
    public OrderHasAlreadyBeenPlacedException() {
        super("Your order has already been placed. Return to main page");
    }
}
