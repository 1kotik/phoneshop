package com.es.core.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException() {
        super("Out of stock");
    }
}
