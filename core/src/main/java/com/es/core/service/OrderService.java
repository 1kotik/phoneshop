package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.Cart;
import com.es.core.model.Order;

public interface OrderService {
    Order createOrder(Cart cart);
    void placeOrder(Order order) throws OutOfStockException;
}
