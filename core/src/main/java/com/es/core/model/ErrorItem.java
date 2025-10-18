package com.es.core.model;

public class ErrorItem {
    Object enteredValue;
    String message;

    public ErrorItem() {}

    public ErrorItem(Object enteredValue, String message) {
        this.enteredValue = enteredValue;
        this.message = message;
    }

    public Object getEnteredValue() {
        return enteredValue;
    }

    public void setEnteredValue(Object enteredValue) {
        this.enteredValue = enteredValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
