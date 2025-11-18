package com.es.core.service;

import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.CartTotals;
import com.es.core.model.ErrorItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CartService {
    Cart getCart();
    void addPhone(Long phoneId, Integer quantity);
    Map<Long, ErrorItem> update(Map<Long, Integer> items);
    void remove(Long phoneId);
    CartTotals getCartTotals();
    Map<Long, Integer> getCartItemsMap(List<? extends CartItem> cartItems);
    void removeByPhoneIdSet(Collection<Long> phoneIds);
    void clearCart();
    Map<String, String> b2bInsert(Map<String, Integer> items);
}
