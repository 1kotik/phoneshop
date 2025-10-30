package com.es.core.dao;

import com.es.core.model.OrderItem;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class JdbcOrderItemDao implements OrderItemDao {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SimpleJdbcInsert jdbcInsertOrderItem;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void insertAll(Collection<OrderItem> items, Long orderId) {
        SqlParameterSource[] params = items.stream()
                .map(item -> getInsertParameters(item, orderId))
                .toArray(SqlParameterSource[]::new);
        jdbcInsertOrderItem.executeBatch(params);
    }

    private SqlParameterSource getInsertParameters(OrderItem item, Long orderId) {
        Map<String, Object> params = new HashMap<>();
        PhoneListItem phone = item.getPhone();
        if (phone == null) {
            phone = new PhoneListItem();
        }
        params.put("orderId", orderId);
        params.put("phoneId", phone.getId());
        params.put("quantity", item.getQuantity());
        return new MapSqlParameterSource(params);
    }
}
