package com.es.core.dao;

import com.es.core.enums.OrderStatus;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.util.OrderBriefInfoRowMapper;
import com.es.core.util.OrderResultSetExtractor;
import com.es.core.util.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class JdbcOrderDao implements OrderDao {
    private final SimpleJdbcInsert jdbcInsertOrder;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final OrderItemDao orderItemDao;

    @Autowired
    public JdbcOrderDao(SimpleJdbcInsert jdbcInsertOrder, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                        OrderItemDao orderItemDao) {
        this.jdbcInsertOrder = jdbcInsertOrder;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.orderItemDao = orderItemDao;
    }

    @Override
    @Transactional
    public Long save(Order order) {
        SqlParameterSource params = buildInsertParameters(order);
        Long id = jdbcInsertOrder.executeAndReturnKey(params).longValue();
        orderItemDao.insertAll(order.getOrderItems(), id);
        return id;
    }

    @Override
    public List<OrderBriefInfo> findAll() {
        return namedParameterJdbcTemplate.query(SqlUtils.Order.FIND_ALL_ORDERS_QUERY, new OrderBriefInfoRowMapper());
    }

    @Override
    public Optional<Order> findById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource("orderId", id);
        Order order = namedParameterJdbcTemplate
                .query(SqlUtils.Order.FIND_BY_ID_QUERY, params, new OrderResultSetExtractor());
        return Optional.ofNullable(order);
    }

    @Override
    public int updateOrderStatus(Long id, OrderStatus status) {
        SqlParameterSource params = new MapSqlParameterSource(
                Map.of("orderId", id, "orderStatus", status.getValue()));
        return namedParameterJdbcTemplate.update(SqlUtils.Order.UPDATE_ORDER_STATUS_QUERY, params);
    }

    @Override
    public Optional<Order> findBySecureId(UUID secureId) {
        SqlParameterSource params = new MapSqlParameterSource("secureOrderId", secureId);
        Order order = namedParameterJdbcTemplate
                .query(SqlUtils.Order.FIND_BY_SECURE_ID_QUERY, params, new OrderResultSetExtractor());
        return Optional.ofNullable(order);
    }

    private SqlParameterSource buildInsertParameters(Order order) {
        Map<String, Object> params = new HashMap<>();
        OrderCustomerInfo customerInfo = order.getCustomerInfo();

        if (customerInfo == null) {
            customerInfo = new OrderCustomerInfo();
        }

        params.put("secureId", order.getSecureId());
        params.put("customerFirstName", customerInfo.getFirstName());
        params.put("customerLastName", customerInfo.getLastName());
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
