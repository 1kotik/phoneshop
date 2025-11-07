package com.es.core.service;

import com.es.core.dao.OrderDao;
import com.es.core.enums.OrderStatus;
import com.es.core.exception.InconsistentOrderException;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.model.OrderItem;
import com.es.core.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DefaultOrderService implements OrderService {
    private final StockService stockService;
    private final CartService cartService;
    private final OrderDao orderDao;
    @Value("${delivery.price}")
    private BigDecimal deliveryPrice;

    @Autowired
    public DefaultOrderService(StockService stockService, CartService cartService, OrderDao orderDao) {
        this.stockService = stockService;
        this.cartService = cartService;
        this.orderDao = orderDao;
    }

    @Override
    public Order createOrder() {
        Order order = new Order();
        BigDecimal subtotal = cartService.getCart().getTotalPrice();
        order.setOrderItems(cartService.getCart().getCartItems().stream().map(OrderItem::new).toList());
        order.setSubtotal(subtotal);
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(subtotal.add(deliveryPrice));
        order.setStatus(OrderStatus.NEW);
        order.setSecureId(UUID.randomUUID());
        return order;
    }

    @Override
    @Transactional
    public Order placeOrder(Order oldOrder, OrderCustomerInfo customerInfo) {
        Order order = createOrder();
        checkStock(order);
        compareOrders(oldOrder, order);
        order.setCustomerInfo(customerInfo);
        Long id = orderDao.save(order);
        order.setId(id);
        stockService.updateStocks(cartService.getCartItemsMap(order.getOrderItems()), order.getStatus());
        cartService.clearCart();
        return order;
    }

    @Override
    public List<OrderBriefInfo> findAll() {
        return orderDao.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus newStatus) {
        int affectedRows = orderDao.updateOrderStatus(id, newStatus);
        if (affectedRows == 0) {
            throw new OrderNotFoundException();
        }
    }

    @Override
    public Order findBySecureId(UUID secureId) {
        return orderDao.findBySecureId(secureId).orElseThrow(OrderNotFoundException::new);
    }

    private void checkStock(Order order) {
        Map<Long, Integer> itemIdQuantityMap = cartService.getCartItemsMap(cartService.getCart().getCartItems());
        List<Stock> stocks = stockService.findByPhoneIdSet(itemIdQuantityMap.keySet());
        List<Long> itemIdsToRemove = stocks.stream()
                .filter(stock -> itemIdQuantityMap.get(stock.getPhoneId()) > stock.getStock())
                .map(Stock::getPhoneId)
                .toList();
        cartService.removeByPhoneIdSet(itemIdsToRemove);
        if (!cartService.getCart().getTotalPrice().equals(order.getSubtotal())) {
            throw new OutOfStockException();
        }
    }

    private void compareOrders(Order oldOrder, Order newOrder) {
        Map<Long, Integer> oldItems = cartService.getCartItemsMap(oldOrder.getOrderItems());
        Map<Long, Integer> newItems = cartService.getCartItemsMap(newOrder.getOrderItems());
        if (!newItems.equals(oldItems)) {
            throw new InconsistentOrderException();
        }
    }
}
