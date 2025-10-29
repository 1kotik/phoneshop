package com.es.core.dao;

import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JdbcOrderDao implements OrderDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SimpleJdbcInsert jdbcInsertOrder;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Resource
    private OrderItemDao orderItemDao;

    @Override
    @Transactional
    public Long save(Order order) {
        SqlParameterSource params = getInsertParameters(order);
        Long id = jdbcInsertOrder.executeAndReturnKey(params).longValue();
        orderItemDao.insertAll(order.getOrderItems(), id);
        return id;
    }

    SqlParameterSource getInsertParameters(Order order) {
        Map<String, Object> params = new HashMap<>();
        OrderCustomerInfo customerInfo = order.getCustomerInfo();

        if (customerInfo == null) {
            customerInfo = new OrderCustomerInfo();
        }

        params.put("secureId", order.getSecureId());
        params.put("customerName", String.format("%s %s",
                customerInfo.getFirstName(), customerInfo.getLastName()));
        params.put("contactPhoneNo", customerInfo.getContactPhoneNo());
        params.put("deliveryAddress", customerInfo.getDeliveryAddress());
        params.put("dateOfRegistration", LocalDateTime.now());
        params.put("totalPrice", order.getTotalPrice());
        params.put("status", order.getStatus().getValue());
        params.put("subtotal", order.getSubtotal());
        params.put("deliveryPrice", order.getDeliveryPrice());
        params.put("additionalInformation", customerInfo.getAdditionalInformation());
        return new MapSqlParameterSource(params);
    }
}
