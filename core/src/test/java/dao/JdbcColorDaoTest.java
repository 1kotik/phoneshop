package dao;

import com.es.core.dao.JdbcColorDao;
import com.es.core.model.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
        PhoneTestUtils.loadTestData(jdbcTemplate);
        testColors = List.of(new Color(1L, "code1"), new Color(null, "code2"));
    }

    @Test
    void shouldInsertColors() {
        List<Long> colorIds = jdbcColorDao.saveAll(this.testColors);
        assertEquals(2, colorIds.size());
    }

    @Test
    void shouldUpdateColors() {
        Color colorToUpdate = new Color(1000L, "update_code");
        List<Long> colorIds = jdbcColorDao.saveAll(List.of(colorToUpdate));
        assertEquals(2, jdbcColorDao.findAll().size());
        assertEquals(1, colorIds.size());
    }

    @Test
    void shouldGetColorsMappedToPhoneIds() {
        Map<Long, Set<Color>> colorMap = jdbcColorDao.findColorsByPhoneIds(Set.of(1000L, 1001L));
        assertEquals(2, colorMap.size());
        assertEquals(4, colorMap.values().stream()
                .map(Set::size).reduce(0, Integer::sum));
    }

    @Test
    void shouldGetEmptyMapIfSetOfIdsIsNull() {
        Map<Long, Set<Color>> colorMap = jdbcColorDao.findColorsByPhoneIds(null);
        assertEquals(0, colorMap.size());
    }
}
