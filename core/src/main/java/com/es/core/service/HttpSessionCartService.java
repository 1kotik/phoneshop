package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.CartTotals;
import com.es.core.model.ErrorItem;
import com.es.core.model.Phone;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private StockService stockService;
    @Resource
    private PhoneService phoneService;
    @Resource
    private ReadWriteLock cartLock;
    private Map<Long, ErrorItem> errors = new HashMap<>();

    @Override
    public Cart getCart() {
        cartLock.readLock().lock();
        try {
            return cart;
        } finally {
            cartLock.readLock().unlock();
        }
    }

    @Override
    public void addPhone(Long phoneId, Integer quantity) {
        cartLock.writeLock().lock();
        try {
            getItemInCart(phoneId).ifPresentOrElse(
                    item -> updateItemIfAlreadyInCart(item, item.getQuantity() + quantity),
                    () -> addItemIfNotInCart(phoneId, quantity));
            calculateTotals();
        } finally {
            cartLock.writeLock().unlock();
        }
    }

    @Override
    public Map<Long, ErrorItem> update(Map<Long, Integer> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyMap();
        }
        cartLock.writeLock().lock();
        errors.clear();
        try {
            updateCartItems(items);
            calculateTotals();
        } finally {
            cartLock.writeLock().unlock();
        }
        return errors;
    }

    @Override
    public void remove(Long phoneId) {
        cartLock.writeLock().lock();
        try {
            CartItem cartItem = getItemInCart(phoneId)
                    .orElseThrow(() -> new PhoneNotFoundException(phoneId));
            stockService.releaseItems(phoneId, cartItem.getQuantity());
            cart.getCartItems().remove(cartItem);
            calculateTotals();
        } catch (PhoneNotFoundException | IllegalArgumentException ignored) {

        } finally {
            cartLock.writeLock().unlock();
        }
    }

    @Override
    public CartTotals getCartTotals() {
        cartLock.readLock().lock();
        try {
            return new CartTotals(cart.getTotalQuantity(), cart.getTotalPrice());
        } finally {
            cartLock.readLock().unlock();
        }
    }

    private void updateCartItems(Map<Long, Integer> items) {
        for (Map.Entry<Long, Integer> item : items.entrySet()) {
            try {
                getItemInCart(item.getKey()).ifPresentOrElse(phone -> updateItemIfAlreadyInCart(phone, item.getValue()),
                        () -> addItemIfNotInCart(item.getKey(), item.getValue()));
            } catch (OutOfStockException | IllegalArgumentException e) {
                errors.put(item.getKey(), new ErrorItem(String.valueOf(item.getValue()), e.getMessage()));
            }
        }
    }

    private Optional<CartItem> getItemInCart(Long phoneId) {
        return cart.getCartItems().stream()
                .filter(item -> phoneId.equals(item.getPhone().getId()))
                .findFirst();
    }

    private void addItemIfNotInCart(Long phoneId, Integer quantity) {
        Phone phone = phoneService.get(phoneId);
        stockService.reserveItems(phoneId, quantity);
        cart.getCartItems().add(new CartItem(phone, quantity));
    }

    private void updateItemIfAlreadyInCart(CartItem cartItem, Integer quantity) {
        int quantityDiff = quantity - cartItem.getQuantity();
        if (quantityDiff > 0) {
            stockService.reserveItems(cartItem.getPhone().getId(), quantityDiff);
        } else if (quantityDiff < 0) {
            stockService.releaseItems(cartItem.getPhone().getId(), -quantityDiff);
        } else {
            return;
        }
        cartItem.setQuantity(quantity);
    }

    private void calculateTotals() {
        Integer totalQuantity = cart.getCartItems().stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalQuantity(totalQuantity);
        cart.setTotalPrice(totalPrice);
    }
}
