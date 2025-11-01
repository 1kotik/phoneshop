package com.es.core.util;

import com.es.core.enums.OrderStatus;
import com.es.core.model.OrderBriefInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrderBriefInfoRowMapper implements RowMapper<OrderBriefInfo> {
    @Override
    public OrderBriefInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderBriefInfo orderBriefInfo = new OrderBriefInfo();
        LocalDateTime dateOfRegistration = rs.getTimestamp("dateOfRegistration").toLocalDateTime();
        orderBriefInfo.setDateOfRegistration(dateOfRegistration);
        orderBriefInfo.setId(rs.getLong(SqlUtils.Order.ORDER_ID));
        orderBriefInfo.setCustomerFirstName(rs.getString("customerFirstName"));
        orderBriefInfo.setCustomerLastName(rs.getString("customerLastName"));
        orderBriefInfo.setContactPhoneNo(rs.getString("contactPhoneNo"));
        orderBriefInfo.setDeliveryAddress(rs.getString("deliveryAddress"));
        orderBriefInfo.setTotalPrice(rs.getBigDecimal("totalPrice"));
        orderBriefInfo.setStatus(OrderStatus.getOrderStatus(rs.getString("status")));
        return orderBriefInfo;
    }
}
