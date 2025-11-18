package com.es.core.service;

import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.RemoveCartItemException;
import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.CartTotals;
import com.es.core.model.ErrorItem;
import com.es.core.model.PhoneIdAndModelDto;
import com.es.core.model.PhoneListItem;
import com.es.core.util.LogMessageCreator;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

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
    @Resource(name = "cartValidationErrors")
    private Map<Long, ErrorItem> errors;
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionCartService.class);

    @Override
    public Cart getCart() {
        return cart;
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
        } catch (PhoneNotFoundException e) {
            logger.warn(LogMessageCreator.createExceptionMessage(e));
            throw new RemoveCartItemException();
        } finally {
            cartLock.writeLock().unlock();
        }
    }

    @Override
    public CartTotals getCartTotals() {
        return new CartTotals(cart.getTotalQuantity(), cart.getTotalPrice());
    }

    private void updateCartItems(Map<Long, Integer> items) {
        for (Map.Entry<Long, Integer> item : items.entrySet()) {
            try {
                getItemInCart(item.getKey()).ifPresentOrElse(phone -> updateItemIfAlreadyInCart(phone, item.getValue()),
                        () -> addItemIfNotInCart(item.getKey(), item.getValue()));
            } catch (OutOfStockException e) {
                errors.put(item.getKey(), new ErrorItem(String.valueOf(item.getValue()), e.getMessage()));
            }
        }
    }

    @Override
    public Map<Long, Integer> getCartItemsMap(List<? extends CartItem> cartItems) {
        return cartItems.stream()
                .collect(Collectors.toMap(item -> item.getPhone().getId(), CartItem::getQuantity));
    }

    @Override
    public void removeByPhoneIdSet(Collection<Long> phoneIds) {
        cartLock.writeLock().lock();
        try {
            List<CartItem> itemsToRemove = cart.getCartItems().stream()
                    .filter(item -> phoneIds.contains(item.getPhone().getId()))
                    .toList();
            if (!itemsToRemove.isEmpty()) {
                stockService.releaseItemsByPhoneIdMap(getCartItemsMap(itemsToRemove));
                cart.getCartItems().removeAll(itemsToRemove);
                calculateTotals();
            }
        } finally {
            cartLock.writeLock().unlock();
        }
    }

    @Override
    public void clearCart() {
        cart.getCartItems().clear();
        cart.setTotalQuantity(0);
        cart.setTotalPrice(BigDecimal.ZERO);
    }

    @Override
    public Map<String, ErrorItem> b2bInsert(Map<String, Integer> items) {
        cartLock.writeLock().lock();
        Map<String, ErrorItem> insertErrors = new HashMap<>();
        try {
            List<PhoneIdAndModelDto> phones = phoneService.findPhonesByModelList(items.keySet());
            Map<Long, Integer> convertedItems = convertB2BInsertMap(items, phones, insertErrors);
            addAllItems(convertedItems, phones, insertErrors);
            calculateTotals();
        } finally {
            cartLock.writeLock().unlock();
        }
        return insertErrors;
    }

    private void addAllItems(Map<Long, Integer> items, List<PhoneIdAndModelDto> phones, Map<String, ErrorItem> errors) {
        for (Map.Entry<Long, Integer> item : items.entrySet()) {
            try {
                getItemInCart(item.getKey()).ifPresentOrElse(phone ->
                                updateItemIfAlreadyInCart(phone, item.getValue() + phone.getQuantity()),
                        () -> addItemIfNotInCart(item.getKey(), item.getValue()));
            } catch (OutOfStockException e) {
                errors.put(findItemInListById(phones, item).getModel(),
                        new ErrorItem(item.getValue(), e.getMessage()));
            }
        }
    }

    private Optional<CartItem> getItemInCart(Long phoneId) {
        return cart.getCartItems().stream()
                .filter(item -> phoneId.equals(item.getPhone().getId()))
                .findFirst();
    }

    private void addItemIfNotInCart(Long phoneId, Integer quantity) {
        PhoneListItem phone = phoneService.getBriefInfoById(phoneId);
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

    private Map<Long, Integer> convertB2BInsertMap(Map<String, Integer> items, List<PhoneIdAndModelDto> phones,
                                                   Map<String, ErrorItem> errors) {
        Map<Long, Integer> newItems = new HashMap<>();
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            Optional<PhoneIdAndModelDto> phone = findItemInListByModel(phones, item);
            if (phone.isPresent()) {
                newItems.put(phone.get().getId(), item.getValue());
            } else {
                errors.put(item.getKey(), new ErrorItem(item.getValue(), "Item not found"));
            }
        }
        return newItems;
    }

    private Optional<PhoneIdAndModelDto> findItemInListByModel(List<PhoneIdAndModelDto> phones, Map.Entry<String, Integer> item) {
        return phones.stream()
                .filter(phone -> phone.getModel().equals(item.getKey()))
                .findFirst();
    }

    private PhoneIdAndModelDto findItemInListById(List<PhoneIdAndModelDto> phones, Map.Entry<Long, Integer> item) {
        return phones.stream()
                .filter(phone -> phone.getId().equals(item.getKey()))
                .findFirst()
                .orElseThrow(() -> new PhoneNotFoundException(item.getKey()));
    }
}
