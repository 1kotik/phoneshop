package com.es.phoneshop.web.model;

public class B2BErrorDto {
    private String enteredModel;
    private String enteredQuantity;
    private String message;

    public B2BErrorDto() {

    }

    public B2BErrorDto(String enteredModel, String enteredQuantity, String message) {
        this.enteredModel = enteredModel;
        this.enteredQuantity = enteredQuantity;
        this.message = message;
    }

    public String getEnteredModel() {
        return enteredModel;
    }

    public void setEnteredModel(String enteredModel) {
        this.enteredModel = enteredModel;
    }

    public String getEnteredQuantity() {
        return enteredQuantity;
    }

    public void setEnteredQuantity(String enteredQuantity) {
        this.enteredQuantity = enteredQuantity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
