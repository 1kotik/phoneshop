package service;

import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.Stock;
import com.es.core.service.DefaultStockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultStockServiceTest {
    @Mock
    private StockDao stockDao;
    @InjectMocks
    private DefaultStockService defaultStockService;

    @Test
    void shouldGetPhone() {
        Long phoneId = 1L;
        when(stockDao.findByPhoneId(phoneId)).thenReturn(Optional.of(new Stock(phoneId, 10, 1)));
        Stock stock = defaultStockService.findByPhoneId(phoneId);
        assertEquals(phoneId, stock.getPhoneId());
    }

    @Test
    void shouldThrowPhoneNotFoundExceptionWhenFindingStock() {
        when(stockDao.findByPhoneId(1L)).thenReturn(Optional.empty());
        assertThrows(StockNotFoundException.class, () -> defaultStockService.findByPhoneId(1L));
    }

    @Test
    void shouldSaveStock() {
        Stock stock = new Stock(1L, 10, 1);
        doNothing().when(stockDao).save(stock);
        assertDoesNotThrow(() -> defaultStockService.save(stock));
    }

    @Test
    void shouldThrowPhoneNotFoundExceptionWhenSavingStock() {
        Stock stock = new Stock(1L, 10, 1);
        doThrow(DataIntegrityViolationException.class).when(stockDao).save(stock);
        assertThrows(PhoneNotFoundException.class, () -> defaultStockService.save(stock));
    }

    @Test
    void shouldReserveItems() {
        Long phoneId = 1L;
        Integer quantity = 10;
        Stock stock = new Stock(phoneId, 20, 10);
        when(stockDao.findByPhoneId(phoneId)).thenReturn(Optional.of(stock));
        doNothing().when(stockDao).save(stock);
        assertDoesNotThrow(() -> defaultStockService.reserveItems(phoneId, quantity));
    }

    @Test
    void shouldThrowOutOfStockException() {
        Long phoneId = 1L;
        Integer quantity = 10;
        Stock stock = new Stock(phoneId, 20, 15);
        when(stockDao.findByPhoneId(phoneId)).thenReturn(Optional.of(stock));
        assertThrows(OutOfStockException.class, () -> defaultStockService.reserveItems(phoneId, quantity));
    }

    @Test
    void shouldReleaseItems() {
        Long phoneId = 1L;
        Integer quantity = 10;
        Stock stock = new Stock(phoneId, 20, 10);
        when(stockDao.findByPhoneId(phoneId)).thenReturn(Optional.of(stock));
        doNothing().when(stockDao).save(stock);
        assertDoesNotThrow(() -> defaultStockService.releaseItems(phoneId, quantity));
    }
}
