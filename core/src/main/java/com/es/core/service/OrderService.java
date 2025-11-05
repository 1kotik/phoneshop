package com.es.core.service;

import com.es.core.enums.OrderStatus;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.model.OrderCustomerInfo;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder();
    Order placeOrder(Order order, OrderCustomerInfo customerInfo) throws OutOfStockException;
    List<OrderBriefInfo> findAll();
    Order findById(Long id);
    void updateOrderStatus(Long id, OrderStatus status);
    Order findBySecureId(UUID secureId);
}
