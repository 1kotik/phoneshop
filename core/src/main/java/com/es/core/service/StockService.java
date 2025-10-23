package com.es.core.service;

import com.es.core.model.Stock;

public interface StockService {
    Stock findByPhoneId(Long phoneId);
    void save(Stock stock);
    void reserveItems(Long phoneId, Integer quantity);
    void releaseItems(Long phoneId, Integer quantity);
}
