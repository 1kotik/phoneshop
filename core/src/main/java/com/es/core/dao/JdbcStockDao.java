package com.es.core.dao;

import com.es.core.model.Stock;
import com.es.core.util.SqlUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JdbcStockDao implements StockDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertStock;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcStockDao(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsertStock,
                        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsertStock = jdbcInsertStock;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Stock> findByPhoneId(Long phoneId) {
        String sql = SqlUtils.Stock.FIND_BY_PHONE_ID_QUERY;
        List<Stock> stock = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Stock.class), phoneId);
        return stock.stream().findFirst();
    }

    @Override
    public void save(Stock stock) {
        findByPhoneId(stock.getPhoneId())
                .ifPresentOrElse(s -> updateStock(stock), () -> insertStock(stock));
    }

    public void insertStock(Stock stock) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stock);
        jdbcInsertStock.execute(params);
    }

    public void updateStock(Stock stock) {
        String sql = SqlUtils.Stock.UPDATE_STOCK_QUERY;
        SqlParameterSource params = new BeanPropertySqlParameterSource(stock);
        namedParameterJdbcTemplate.update(sql, params);
    }
}
