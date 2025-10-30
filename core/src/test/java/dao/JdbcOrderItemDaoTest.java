package dao;

import com.es.core.dao.JdbcOrderItemDao;
import com.es.core.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/context/testContext-core.xml"})
class JdbcOrderItemDaoTest {
    private final JdbcOrderItemDao jdbcOrderItemDao;
    private static final String DEMODATA_SCRIPT_PATH = "classpath:/db/test-demodata.sql";
    private static final String CLEAN_DEMODATA_SCRIPT_PATH = "classpath:/db/clean-test-demodata.sql";

    @Autowired
    public JdbcOrderItemDaoTest(JdbcOrderItemDao jdbcOrderItemDao) {
        this.jdbcOrderItemDao = jdbcOrderItemDao;
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldInsertAllOrderItems() {
        List<OrderItem> items = PhoneTestUtils.getOrder().getOrderItems();
        items.get(0).getPhone().setId(1000L);
        items.get(1).getPhone().setId(1001L);
        assertDoesNotThrow(() -> jdbcOrderItemDao.insertAll(items, 1L));
    }

    @Test
    @Sql(scripts = {CLEAN_DEMODATA_SCRIPT_PATH, DEMODATA_SCRIPT_PATH},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into orderItems(id, orderId, phoneId, quantity) values(1, 1, 1000, 2)")
    void shouldThrowDataIntegrityViolationException() {
        List<OrderItem> items = PhoneTestUtils.getOrder().getOrderItems();
        items.get(0).getPhone().setId(1000L);
        items.get(1).getPhone().setId(1001L);
        assertThrows(DataIntegrityViolationException.class, () -> jdbcOrderItemDao.insertAll(items, 1L));
    }
}
