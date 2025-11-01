package com.es.core.dao;

import com.es.core.enums.OrderStatus;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderDao {
    Long save(Order order);
    List<OrderBriefInfo> findAll();
    Optional<Order> findById(Long id);
    int updateOrderStatus(Long id, OrderStatus status);
    Optional<Order> findBySecureId(UUID secureId);
}
