package com.es.core.model;

import java.util.UUID;

public class OrderItem extends CartItem {
    private UUID id;
    private UUID orderId;

    public OrderItem(PhoneListItem phone, Integer quantity, UUID id, UUID orderId) {
        super(phone, quantity);
        this.id = id;
        this.orderId = orderId;
    }

    public OrderItem(CartItem cartItem) {
        super(cartItem.getPhone(), cartItem.getQuantity());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
