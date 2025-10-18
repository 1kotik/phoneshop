package com.es.phoneshop.web.model;

import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CartUpdateForm {
    private Map<@NotNull Long, @Min(1) @NotNull Integer> items = new HashMap<>();

    public CartUpdateForm() {}

    public CartUpdateForm(Map<Long, Integer> items) {
        this.items = items;
    }

    public CartUpdateForm(Cart cart) {
        for (CartItem item : cart.getCartItems()) {
            items.put(item.getPhone().getId(), item.getQuantity());
        }
    }

    public Map<Long, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Long, Integer> items) {
        this.items = items;
    }
}
