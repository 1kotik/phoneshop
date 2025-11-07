package com.es.core.model;

public class OrderItem extends CartItem {
    private Long id;
    private Long orderId;

    public OrderItem(PhoneListItem phone, Integer quantity, Long id, Long orderId) {
        super(phone, quantity);
        this.id = id;
        this.orderId = orderId;
    }

    public OrderItem() {

    }

    public OrderItem(CartItem cartItem) {
        super(cartItem.getPhone(), cartItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
