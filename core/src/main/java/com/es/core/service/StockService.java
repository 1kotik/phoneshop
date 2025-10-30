package com.es.core.service;

import com.es.core.enums.OrderStatus;
import com.es.core.model.Stock;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StockService {
    Stock findByPhoneId(Long phoneId);
    void save(Stock stock);
    void reserveItems(Long phoneId, Integer quantity);
    void releaseItems(Long phoneId, Integer quantity);
    void updateStocks(Map<Long, Integer> stocks, OrderStatus orderStatus);
    List<Stock> findByPhoneIdSet(Collection<Long> phoneIds);
    void releaseItemsByPhoneIdMap(Map<Long, Integer> stocks);
    void saveAll(Collection<Stock> stocks);
}
