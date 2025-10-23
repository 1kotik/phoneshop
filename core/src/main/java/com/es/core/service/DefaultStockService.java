package com.es.core.service;

import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.Stock;
import com.es.core.util.AppConstants;
import jakarta.annotation.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultStockService implements StockService {
    @Resource
    private StockDao stockDao;

    @Override
    public Stock findByPhoneId(Long phoneId) {
        return stockDao.findByPhoneId(phoneId)
                .orElseThrow(() -> new StockNotFoundException(phoneId));
    }

    @Override
    public void save(Stock stock) {
        try {
            stockDao.save(stock);
        } catch (DataIntegrityViolationException e) {
            throw new PhoneNotFoundException(stock.getPhoneId());
        }
    }

    @Override
    @Transactional
    public void reserveItems(Long phoneId, Integer quantity) {
        Stock stock = findByPhoneId(phoneId);

        if (stock.getStock() - stock.getReserved() < quantity) {
            throw new OutOfStockException();
        }

        stock.setReserved(stock.getReserved() + quantity);
        stockDao.save(stock);
    }

    @Override
    @Transactional
    public void releaseItems(Long phoneId, Integer quantity) {
        Stock stock = findByPhoneId(phoneId);

        if(stock.getReserved() < quantity) {
            stock.setReserved(0);
        } else {
            stock.setReserved(stock.getReserved() - quantity);
        }

        stockDao.save(stock);
    }

}
