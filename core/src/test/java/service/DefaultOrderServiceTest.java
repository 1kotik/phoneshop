package service;

import com.es.core.dao.OrderDao;
import com.es.core.enums.OrderStatus;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(cartService.getCart()).thenReturn(cart);
        when(cartService.getCart().getTotalPrice()).thenReturn(BigDecimal.TEN);
        when(cartService.getCart().getCartItems()).thenReturn(items);
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
        when(cartService.getCart()).thenReturn(cart);
        when(cartService.getCart().getTotalPrice()).thenReturn(BigDecimal.TEN);
        when(cartService.getCart().getCartItems()).thenReturn(items);
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

    @Test
    void shouldFindAll() {
        List<OrderBriefInfo> expectedResult = PhoneTestUtils.getOrderBriefInfoList();
        when(orderDao.findAll()).thenReturn(expectedResult);
        List<OrderBriefInfo> actualResult = defaultOrderService.findAll();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldFindById() {
        Long orderId = 1L;
        Order expectedOrder = PhoneTestUtils.getOrder();
        when(orderDao.findById(orderId)).thenReturn(Optional.of(expectedOrder));
        Order actualOrder = defaultOrderService.findById(orderId);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenFindingById() {
        Long orderId = 2L;
        when(orderDao.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> defaultOrderService.findById(orderId));
    }

    @Test
    void shouldFindBySecureId() {
        UUID secureId = UUID.randomUUID();
        Order expectedOrder = PhoneTestUtils.getOrder();
        when(orderDao.findBySecureId(secureId)).thenReturn(Optional.of(expectedOrder));
        Order actualOrder = defaultOrderService.findBySecureId(secureId);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenFindingBySecureId() {
        UUID secureId = UUID.randomUUID();
        when(orderDao.findBySecureId(secureId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> defaultOrderService.findBySecureId(secureId));
    }

    @Test
    void shouldUpdateOrder() {
        OrderStatus newStatus = OrderStatus.DELIVERED;
        Long orderId = 1L;
        when(orderDao.updateOrderStatus(orderId, newStatus)).thenReturn(1);
        assertDoesNotThrow(() -> defaultOrderService.updateOrderStatus(orderId, newStatus));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenUpdatingOrder() {
        OrderStatus newStatus = OrderStatus.DELIVERED;
        Long orderId = 2L;
        when(orderDao.updateOrderStatus(orderId, newStatus)).thenReturn(0);
        assertThrows(OrderNotFoundException.class,
                () -> defaultOrderService.updateOrderStatus(orderId, newStatus));
    }
}
