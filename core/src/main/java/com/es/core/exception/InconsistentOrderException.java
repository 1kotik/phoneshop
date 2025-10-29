package com.es.core.exception;

public class InconsistentOrderException extends RuntimeException {
    public InconsistentOrderException() {
        super("Inconsistent order. Probably cart was updated");
    }
}
