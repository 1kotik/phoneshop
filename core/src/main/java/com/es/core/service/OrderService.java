package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;

public interface OrderService {
    Order createOrder();
    Order placeOrder(Order order, OrderCustomerInfo customerInfo) throws OutOfStockException;
}
