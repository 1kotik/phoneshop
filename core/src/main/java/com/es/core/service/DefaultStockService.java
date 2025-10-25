package com.es.core.service;

import com.es.core.dao.StockDao;
import com.es.core.enums.OrderStatus;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.Stock;
import jakarta.annotation.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        Integer reservedQuantity = stock.getReserved();

        if (stock.getStock() - reservedQuantity < quantity) {
            throw new OutOfStockException();
        }

        stock.setReserved(reservedQuantity + quantity);
        stockDao.save(stock);
    }

    @Override
    @Transactional
    public void releaseItems(Long phoneId, Integer quantityToRelease) {
        Stock stock = findByPhoneId(phoneId);
        Integer oldReserved = stock.getReserved();
        stock.setReserved(Math.max(oldReserved - quantityToRelease, 0));
        stockDao.save(stock);
    }

    @Override
    @Transactional
    public void updateStocks(Map<Long, Integer> stockMap, OrderStatus orderStatus) {
        boolean isOrderNew = OrderStatus.NEW.equals(orderStatus);
        boolean isOrderRejected = OrderStatus.REJECTED.equals(orderStatus);
        if (!isOrderNew && !isOrderRejected) {
            return;
        }
        List<Stock> stockList = stockDao.findByPhoneIdSet(stockMap.keySet());
        for (Stock stock : stockList) {
            Integer deltaQuantity = stockMap.get(stock.getPhoneId());
            if (isOrderNew) {
                decreaseStock(stock, deltaQuantity);
            } else {
                stock.setStock(stock.getStock() + deltaQuantity);
                stock.setReserved(stock.getReserved() - deltaQuantity);
            }
        }
        saveAll(stockList);
    }

    @Override
    public List<Stock> findByPhoneIdSet(Collection<Long> phoneIds) {
        return stockDao.findByPhoneIdSet(phoneIds);
    }

    @Override
    @Transactional
    public void releaseItemsByPhoneIdMap(Map<Long, Integer> stockMap) {
        List<Stock> stockList = stockDao.findByPhoneIdSet(stockMap.keySet());
        for (Stock stock : stockList) {
            Integer oldReserved = stock.getReserved();
            Integer quantityToRelease = stockMap.get(stock.getPhoneId());
            stock.setReserved(Math.max(oldReserved - quantityToRelease, 0));
        }
        saveAll(stockList);
    }

    @Override
    public void saveAll(Collection<Stock> stocks) {
        stockDao.saveAll(stocks);
    }

    private void decreaseStock(Stock stock, Integer deltaQuantity) {
        Integer oldStock = stock.getStock();
        if (oldStock - deltaQuantity < 0) {
            throw new OutOfStockException();
        }
        stock.setStock(oldStock - deltaQuantity);
        stock.setReserved(oldStock - deltaQuantity);
    }
}
