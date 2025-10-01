package com.es.core.exception;

public class PhoneNotFoundException extends RuntimeException {
    public PhoneNotFoundException(Long id) {
        super("Phone with " + id + " not found");
    }
}
