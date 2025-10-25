package com.es.core.dao;

import com.es.core.model.OrderItem;

import java.util.Collection;

public interface OrderItemDao {
    void insertAll(Collection<OrderItem> items, Long orderId);
}
