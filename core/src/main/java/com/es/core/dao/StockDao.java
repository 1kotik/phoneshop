package com.es.core.dao;

import com.es.core.model.Stock;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StockDao {
    Optional<Stock> findByPhoneId(Long phoneId);
    void save(Stock stock);
    List<Stock> findByPhoneIdSet(Collection<Long> stockIds);
    void saveAll(Collection<Stock> stocks);
}
