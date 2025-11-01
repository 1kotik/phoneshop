package com.es.core.util;

import com.es.core.enums.OrderStatus;
import com.es.core.model.Color;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.model.OrderItem;
import com.es.core.model.PhoneListItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class OrderRowMapper implements RowMapper<Order> {
    private RowMapper<PhoneListItem> phoneListItemRowMapper = new PhoneListItemRowMapper();

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = collectOrderGeneralInfo(rs);
        Map<Long, List<OrderItem>> orderItems = collectOrderItemsToMap(rs, rowNum);
        orderItems.values().forEach(items -> collectItemColorsToSingleEntity(order, items));
        return order;
    }

    private Order collectOrderGeneralInfo(ResultSet rs) throws SQLException {
        Order order = new Order();
        OrderCustomerInfo customerInfo = new OrderCustomerInfo();
        Long orderId = rs.getLong(SqlUtils.Order.ORDER_ID);

        customerInfo.setFirstName(rs.getString("customerFirstName"));
        customerInfo.setLastName(rs.getString("customerLastName"));
        customerInfo.setAdditionalInformation(rs.getString("additionalInformation"));
        customerInfo.setContactPhoneNo(rs.getString("contactPhoneNo"));
        customerInfo.setDeliveryAddress(rs.getString("deliveryAddress"));
        order.setCustomerInfo(customerInfo);
        order.setId(orderId);
        order.setSecureId(UUID.fromString(rs.getString("secureId")));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setDeliveryPrice(rs.getBigDecimal("deliveryPrice"));
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setStatus(OrderStatus.getOrderStatus(rs.getString("status")));
        return order;
    }

    private Map<Long, List<OrderItem>> collectOrderItemsToMap(ResultSet rs, int rowNum) throws SQLException {
        Map<Long, List<OrderItem>> orderItems = new HashMap<>();
        do {
            OrderItem orderItem = new OrderItem();
            Long orderItemId = rs.getLong("itemId");
            PhoneListItem phone = phoneListItemRowMapper.mapRow(rs, rowNum);
            orderItem.setId(orderItemId);
            orderItem.setOrderId(rs.getLong(SqlUtils.Order.ORDER_ID));
            orderItem.setQuantity(rs.getInt("quantity"));
            orderItem.setPhone(phone);
            orderItems.computeIfAbsent(orderItemId, k -> new ArrayList<>()).add(orderItem);
        } while (rs.next());
        return orderItems;
    }

    private void collectItemColorsToSingleEntity(Order order, List<OrderItem> orderItems) {
        Optional<OrderItem> result = orderItems.stream().findFirst();
        if (result.isPresent()) {
            Set<Color> colors = new HashSet<>();
            orderItems.forEach(item -> colors.addAll(item.getPhone().getColors()));
            result.get().getPhone().setColors(colors);
            order.getOrderItems().add(result.get());
        }
    }
}
