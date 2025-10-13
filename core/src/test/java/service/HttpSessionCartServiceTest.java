package service;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.Cart;
import com.es.core.model.CartItem;
import com.es.core.service.HttpSessionCartService;
import com.es.core.service.PhoneService;
import com.es.core.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.PhoneTestUtils;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpSessionCartServiceTest {
    @Mock
    private PhoneService phoneService;
    @Mock
    private StockService stockService;
    @Mock
    private Cart cart;
    @Mock
    private ReadWriteLock cartLock;
    @Mock
    private Lock writeLock;
    @InjectMocks
    private HttpSessionCartService httpSessionCartService;
    private List<CartItem> cartItems = PhoneTestUtils.getCartList();

    @Test
    void shouldAddNewPhoneToCart() {
        Long phoneId = 3L;
        when(cartLock.writeLock()).thenReturn(writeLock);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        when(cart.getCartItems()).thenReturn(cartItems);
        when(phoneService.get(phoneId)).thenReturn(PhoneTestUtils.getPhone(phoneId));
        doNothing().when(stockService).reserveItems(phoneId, 2);
        assertDoesNotThrow(() -> httpSessionCartService.addPhone(phoneId, 2));
        assertEquals(3, cart.getCartItems().size());
        verify(cart).setTotalQuantity(6);
    }

    @Test
    void shouldUpdatePhoneQuantityInCart() {
        Long phoneId = 1L;
        when(cartLock.writeLock()).thenReturn(writeLock);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        when(cart.getCartItems()).thenReturn(cartItems);
        doNothing().when(stockService).reserveItems(phoneId, 2);
        assertDoesNotThrow(() -> httpSessionCartService.addPhone(phoneId, 2));
        assertEquals(2, cart.getCartItems().size());
        verify(cart).setTotalQuantity(6);
    }

    @Test
    void shouldThrowOutOfStockException() {
        Long phoneId = 1L;
        when(cartLock.writeLock()).thenReturn(writeLock);
        doNothing().when(writeLock).lock();
        doNothing().when(writeLock).unlock();
        when(cart.getCartItems()).thenReturn(cartItems);
        doThrow(OutOfStockException.class).when(stockService).reserveItems(phoneId, 2);
        assertThrows(OutOfStockException.class, () -> httpSessionCartService.addPhone(phoneId, 2));
    }

}
