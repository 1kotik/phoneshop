package com.es.core.service;

import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.CartTotals;
import com.es.core.model.Phone;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private StockService stockService;
    @Resource
    private PhoneService phoneService;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Integer quantity) {
        getItemInCart(phoneId).ifPresentOrElse(item -> updateItemIfAlreadyInCart(item, quantity),
                () -> addItemIfNotInCart(phoneId, quantity));
    }

    @Override
    public void update(Map<Long, Integer> items) {

    }

    @Override
    public void remove(Long phoneId) {

    }

    @Override
    public CartTotals getCartTotals() {
        return new CartTotals(cart.getTotalQuantity(), cart.getTotalPrice());
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
        calculateTotals();
    }

    private void updateItemIfAlreadyInCart(CartItem cartItem, Integer quantity) {
        stockService.reserveItems(cartItem.getPhone().getId(), quantity);
        cart.getCartItems().stream()
                .filter(item -> item.getPhone().getId().equals(cartItem.getPhone().getId()))
                .forEach(item -> item.setQuantity(item.getQuantity() + quantity));
        calculateTotals();
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
