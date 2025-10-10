package com.es.core.exception;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(Long phoneId) {
        super(String.format("Stock not found with phone id %d", phoneId));
    }
}
