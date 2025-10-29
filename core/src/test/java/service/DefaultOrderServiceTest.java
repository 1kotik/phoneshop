package service;

import com.es.core.dao.OrderDao;
import com.es.core.enums.OrderStatus;
import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.Order;
import com.es.core.model.Stock;
import com.es.core.service.CartService;
import com.es.core.service.DefaultOrderService;
import com.es.core.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.PhoneTestUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultOrderServiceTest {
    @Mock
    private Cart cart;
    @Mock
    private StockService stockService;
    @Mock
    private CartService cartService;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private DefaultOrderService defaultOrderService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field deliveryPriceField = DefaultOrderService.class.getDeclaredField("deliveryPrice");
        deliveryPriceField.setAccessible(true);
        deliveryPriceField.set(defaultOrderService, BigDecimal.ONE);
    }

    @Test
    void shouldGetOrder() {
        Map<Long, Integer> stockMap = Map.of(1L, 2, 2L, 2);
        List<CartItem> items = PhoneTestUtils.getCartList();
        when(cart.getTotalPrice()).thenReturn(BigDecimal.TEN);
        when(cart.getCartItems()).thenReturn(items);
        Order order = defaultOrderService.createOrder();
        assertEquals(order.getOrderItems().size(), stockMap.size());
        assertEquals(order.getTotalPrice(), BigDecimal.valueOf(11));
    }

    @Test
    void shouldPlaceOrder() {
        Order expectedOrder = PhoneTestUtils.getOrder();
        Map<Long, Integer> stockMap = Map.of(1L, 2, 2L, 2);
        List<Stock> stocks = PhoneTestUtils.getStockList();
        List<CartItem> items = PhoneTestUtils.getCartList();
        OrderStatus orderStatus = OrderStatus.NEW;
        when(cart.getTotalPrice()).thenReturn(BigDecimal.TEN);
        when(cart.getCartItems()).thenReturn(items);
        when(cartService.getCartItemsMap(any())).thenReturn(stockMap);
        when(stockService.findByPhoneIdSet(stockMap.keySet())).thenReturn(stocks);
        doNothing().when(cartService).removeByPhoneIdSet(Collections.emptyList());
        doNothing().when(stockService).updateStocks(stockMap, orderStatus);
        when(orderDao.save(any())).thenReturn(1L);
        doNothing().when(cartService).clearCart();
        Order actualOrder = defaultOrderService
                .placeOrder(PhoneTestUtils.getOrder(), PhoneTestUtils.getOrderCustomerInfo());
        assertEquals(actualOrder.getOrderItems().size(), expectedOrder.getOrderItems().size());
        assertEquals(actualOrder.getTotalPrice(), expectedOrder.getTotalPrice());
    }
}
