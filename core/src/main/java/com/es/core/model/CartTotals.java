package com.es.core.model;

import java.math.BigDecimal;

public class CartTotals {
    private Integer totalQuantity;
    private BigDecimal totalPrice;

    public CartTotals() {
    }

    public CartTotals(Integer totalQuantity, BigDecimal totalPrice) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
