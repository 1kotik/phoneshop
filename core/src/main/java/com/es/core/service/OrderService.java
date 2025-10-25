package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;

import java.util.UUID;

public interface OrderService {
    Order createOrder();
    Order placeOrder(OrderCustomerInfo customerInfo) throws OutOfStockException;
}
