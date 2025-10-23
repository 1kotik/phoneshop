package com.es.core.exception;

public class RemoveCartItemException extends RuntimeException {
    public RemoveCartItemException() {
        super("Error occurred while removing item from cart");
    }
}
