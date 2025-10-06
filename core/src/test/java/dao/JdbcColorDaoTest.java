package dao;

import com.es.core.dao.JdbcColorDao;
import com.es.core.model.Color;
import com.es.core.util.SqlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/context/testContext-core.xml"})
class JdbcColorDaoTest {
    private final JdbcColorDao jdbcColorDao;
    private final JdbcTemplate jdbcTemplate;
    private List<Color> testColors;

    @Autowired
    public JdbcColorDaoTest(JdbcColorDao jdbcColorDao, JdbcTemplate jdbcTemplate) {
        this.jdbcColorDao = jdbcColorDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Color.TABLE_NAME);
        testColors = List.of(new Color(1L, "code1"), new Color(null, "code2"));
    }

    @Test
    void shouldInsertColors() {
        List<Long> colorIds = jdbcColorDao.saveAll(this.testColors);
        assertEquals(2, colorIds.size());
    }

    @Test
    void shouldUpdateColors() {
        List<Long> colorIds = jdbcColorDao.saveAll(this.testColors);
        Color colorToUpdate = new Color(colorIds.get(0), "update_code");
        jdbcColorDao.saveAll(List.of(colorToUpdate));
        assertEquals(2, jdbcColorDao.findAll().size());
    }
}
