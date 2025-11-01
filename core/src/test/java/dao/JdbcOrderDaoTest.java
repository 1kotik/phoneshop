package dao;

import com.es.core.dao.JdbcOrderDao;
import com.es.core.enums.OrderStatus;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/context/testContext-core.xml"})
class JdbcOrderDaoTest {
    private JdbcOrderDao jdbcOrderDao;
    private static final String DEMODATA_SCRIPT_PATH = "classpath:/db/test-demodata.sql";
    private static final String CLEAN_DEMODATA_SCRIPT_PATH = "classpath:/db/clean-test-demodata.sql";

    @Autowired
    public JdbcOrderDaoTest(JdbcOrderDao jdbcOrderDao) {
        this.jdbcOrderDao = jdbcOrderDao;
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldSaveOrder() {
        Order order = PhoneTestUtils.getOrder();
        order.setId(2L);
        List<OrderItem> items = order.getOrderItems();
        items.get(0).getPhone().setId(1000L);
        items.get(1).getPhone().setId(1001L);
        assertDoesNotThrow(() -> jdbcOrderDao.save(order));
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindAll() {
        List<OrderBriefInfo> orders = jdbcOrderDao.findAll();
        assertEquals(1, orders.size());
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindById() {
        Optional<Order> order = jdbcOrderDao.findById(1L);
        assertTrue(order.isPresent());
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotFindById() {
        Optional<Order> order = jdbcOrderDao.findById(2L);
        assertTrue(order.isEmpty());
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldFindBySecureId() {
        Optional<Order> order = jdbcOrderDao
                .findBySecureId(UUID.fromString("08728425-46e4-40c2-8586-3583d418b4ec"));
        assertTrue(order.isPresent());
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotFindBySecureId() {
        Optional<Order> order = jdbcOrderDao
                .findBySecureId(UUID.fromString("08728425-46e4-40c2-8586-3583d418b4ed"));
        assertTrue(order.isEmpty());
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldUpdateOrderStatus() {
        int rowsAffected = jdbcOrderDao.updateOrderStatus(1L, OrderStatus.DELIVERED);
        assertEquals(1, rowsAffected);
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldNotUpdateOrderStatusBecauseOrderDoesNotExist() {
        int rowsAffected = jdbcOrderDao.updateOrderStatus(2L, OrderStatus.DELIVERED);
        assertEquals(0, rowsAffected);
    }
}
