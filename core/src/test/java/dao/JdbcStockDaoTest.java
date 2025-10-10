package dao;

import com.es.core.dao.JdbcStockDao;
import com.es.core.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/context/testContext-core.xml"})
class JdbcStockDaoTest {
    private final JdbcStockDao stockDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcStockDaoTest(JdbcStockDao stockDao, JdbcTemplate jdbcTemplate) {
        this.stockDao = stockDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        PhoneTestUtils.loadTestData(jdbcTemplate);
    }

    @Test
    void shouldGetStockWhenPhoneExists() {
        Optional<Stock> stock = stockDao.findByPhoneId(1000L);
        assertTrue(stock.isPresent());
    }

    @ParameterizedTest(name = "{index} - Id is {0}")
    @NullSource
    @ValueSource(longs = 1111)
    void shouldGetEmptyStockWhenIdIsNullOrDoesNotExist(Long phoneId) {
        Optional<Stock> stock = stockDao.findByPhoneId(phoneId);
        assertTrue(stock.isEmpty());
    }

    @Test
    void shouldThrowDataIntegrityViolationException() {
        Stock stock = new Stock(100L, 10, 10);
        assertThrows(DataIntegrityViolationException.class, () -> stockDao.insertStock(stock));
    }

    @ParameterizedTest(name = "{index} - phoneId is {0}")
    @MethodSource("stockSource")
    void shouldSaveStock(Long phoneId, Integer availableItems, Integer reserved) {
        Stock stock = new Stock(phoneId, availableItems, reserved);
        stockDao.save(stock);
        Optional<Stock> savedStock = stockDao.findByPhoneId(stock.getPhoneId());
        assertTrue(savedStock.isPresent());
        assertEquals(stock.getStock(), savedStock.get().getStock());
    }

    @Test
    void shouldUpdateStock() {
        Stock stock = new Stock(1000L, 100, 10);
        stockDao.save(stock);
        Optional<Stock> savedStock = stockDao.findByPhoneId(stock.getPhoneId());
        assertTrue(savedStock.isPresent());
        assertEquals(stock.getStock(), savedStock.get().getStock());
    }

    private static Stream<Arguments> stockSource() {
        return Stream.of(
                Arguments.of(1001L, 100, 10),
                Arguments.of(1000L, 100, 10));
    }
}
