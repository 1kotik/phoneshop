package dao;

import com.es.core.dao.JdbcOrderDao;
import com.es.core.model.Order;
import com.es.core.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
