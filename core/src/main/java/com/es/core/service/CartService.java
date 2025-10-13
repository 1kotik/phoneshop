package com.es.core.service;

import com.es.core.model.Cart;
import com.es.core.model.CartTotals;
import com.es.core.model.Phone;

import java.util.Map;

public interface CartService {

    Cart getCart();

    void addPhone(Long phoneId, Integer quantity);

    /**
     * @param items
     * key: {@link Phone#id}
     * value: quantity
     */
    void update(Map<Long, Integer> items);

    void remove(Long phoneId);
    CartTotals getCartTotals();
}
