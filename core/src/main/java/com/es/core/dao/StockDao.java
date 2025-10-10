package com.es.core.dao;

import com.es.core.model.Stock;

import java.util.Optional;

public interface StockDao {
    Optional<Stock> findByPhoneId(Long phoneId);
    void save(Stock stock);
}
