package com.es.core.service;

import com.es.core.dao.OrderDao;
import com.es.core.enums.OrderStatus;
import com.es.core.exception.OrderHasAlreadyBeenPlacedException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.Cart;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.model.OrderItem;
import com.es.core.model.Stock;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;

@Service
public class DefaultOrderService implements OrderService {
    @Resource
    private Cart cart;
    @Resource
    private StockService stockService;
    @Resource
    private CartService cartService;
    @Resource
    private Order orderToPlace;
    @Resource
    private ReadWriteLock orderLock;
    @Resource
    private OrderDao orderDao;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Override
    public Order createOrder() {
        orderLock.writeLock().lock();
        Order order = new Order();
        try {
            setOrder();
            BeanUtils.copyProperties(orderToPlace, order);
        } finally {
            orderLock.writeLock().unlock();
        }
        return order;
    }

    @Override
    public Order placeOrder(OrderCustomerInfo customerInfo) {
        if (orderToPlace.getSecureId() == null) {
            throw new OrderHasAlreadyBeenPlacedException();
        }
        orderLock.writeLock().lock();
        Order order = new Order();
        try {
            checkStock();
            BeanUtils.copyProperties(orderToPlace, order);
            OrderStatus orderStatus = OrderStatus.NEW;
            order.setCustomerInfo(customerInfo);
            order.setStatus(orderStatus);
            stockService.updateStocks(cartService.getCartItemsMap(order.getOrderItems()), orderStatus);
            Long id = orderDao.save(order);
            order.setId(id);
            cartService.clearCart();
            clearOrder();
        } catch (DataIntegrityViolationException e) {
            throw new OrderHasAlreadyBeenPlacedException();
        } finally {
            orderLock.writeLock().unlock();
        }
        return order;
    }

    private void setOrder() {
        BigDecimal subtotal = cart.getTotalPrice();
        if (subtotal.equals(BigDecimal.ZERO)) {
            throw new OrderHasAlreadyBeenPlacedException();
        }
        UUID secureId = UUID.randomUUID();
        orderToPlace.setOrderItems(cart.getCartItems().stream().map(OrderItem::new).toList());
        orderToPlace.setSubtotal(subtotal);
        orderToPlace.setDeliveryPrice(deliveryPrice);
        orderToPlace.setTotalPrice(subtotal.add(deliveryPrice));
        orderToPlace.setSecureId(secureId);
    }

    private void clearOrder() {
        orderToPlace.setOrderItems(null);
        orderToPlace.setSubtotal(null);
        orderToPlace.setDeliveryPrice(null);
        orderToPlace.setTotalPrice(null);
        orderToPlace.setSecureId(null);
    }

    private void checkStock() {
        Map<Long, Integer> itemIdQuantityMap = cartService.getCartItemsMap(cart.getCartItems());
        List<Stock> stocks = stockService.findByPhoneIdSet(itemIdQuantityMap.keySet());
        List<Long> itemIdsToRemove = stocks.stream()
                .filter(stock -> itemIdQuantityMap.get(stock.getPhoneId()) > stock.getStock())
                .map(Stock::getPhoneId)
                .toList();
        cartService.removeByPhoneIdSet(itemIdsToRemove);
        if (!cart.getTotalPrice().equals(orderToPlace.getSubtotal())) {
            throw new OutOfStockException();
        }
    }
}
