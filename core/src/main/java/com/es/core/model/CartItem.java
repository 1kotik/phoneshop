package com.es.core.model;

public class CartItem {
    protected PhoneListItem phone;
    protected Integer quantity;

    public CartItem() {
    }

    public CartItem(PhoneListItem phone, Integer quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public PhoneListItem getPhone() {
        return phone;
    }

    public void setPhone(PhoneListItem phone) {
        this.phone = phone;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
