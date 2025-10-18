package dao;

import com.es.core.dao.JdbcColorDao;
import com.es.core.dao.JdbcPhoneDao;
import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.model.Color;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListResponse;
import com.es.core.util.SqlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.PhoneTestUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:/context/testContext-core.xml"})
class JdbcPhoneDaoTest {
    private final JdbcPhoneDao phoneDao;
    private final JdbcTemplate jdbcTemplate;
    private final JdbcColorDao jdbcColorDao;
    private Phone testPhone;

    @Autowired
    public JdbcPhoneDaoTest(JdbcPhoneDao jdbcPhoneDao, JdbcTemplate jdbcTemplate, JdbcColorDao jdbcColorDao) {
        this.phoneDao = jdbcPhoneDao;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcColorDao = jdbcColorDao;
    }

    @BeforeEach
    public void setUp() {
        PhoneTestUtils.loadTestData(jdbcTemplate);
        testPhone = PhoneTestUtils.getPhone(1L);
    }

    @Test
    void shouldSaveAndGetPhone() {
        Long phoneId = phoneDao.save(testPhone);
        Optional<Phone> retrievedPhone = phoneDao.get(phoneId);
        assertTrue(retrievedPhone.isPresent());
        Phone phone = retrievedPhone.get();
        assertEquals(testPhone.getBrand(), phone.getBrand());
        assertEquals(testPhone.getModel(), phone.getModel());
        assertEquals(testPhone.getPrice(), phone.getPrice());
    }

    @ParameterizedTest(name = "{index} - Phone existed before updating - {0}")
    @ValueSource(strings = {"true", "false"})
    void shouldUpdatePhone(boolean phoneExists) {
        if (phoneExists) {
            Long id = phoneDao.save(testPhone);
            testPhone.setId(id);
        } else {
            testPhone.setId(1L);
        }

        testPhone.setBrand("new");
        testPhone.setPrice(new BigDecimal(1));
        Long phoneId = phoneDao.save(testPhone);
        Optional<Phone> updatedPhone = phoneDao.get(phoneId);
        assertTrue(updatedPhone.isPresent());
        Phone phone = updatedPhone.get();
        assertEquals("new", phone.getBrand());
        assertEquals(new BigDecimal("1.0"), phone.getPrice());
    }

    @Test
    void shouldFindAll() {
        PhoneListResponse response = phoneDao.findAll("", SortCriteria.BRAND, SortOrder.ASC, 0, 10);
        assertEquals(1, response.getPhones().size());
    }

    @Test
    void shouldReturnEmptyIfPhoneDoesNotExist() {
        Optional<Phone> phone = phoneDao.get(1111L);
        assertTrue(phone.isEmpty());
    }

    @Test
    void shouldReturnEmptyIfKeyIsNull() {
        Optional<Phone> phone = phoneDao.get(null);
        assertTrue(phone.isEmpty());
    }

    @Test
    void shouldSavePhoneColorRelations() {
        String verifyingSql = String.format("select count(*) from %s where %s = ?",
                SqlUtils.Phone.PHONES_COLORS_RELATIONS_TABLE_NAME, SqlUtils.Phone.PHONES_COLORS_RELATIONS_PHONE_ID);
        List<Color> colors = List.of(new Color(null, "red"), new Color(null, "blue"));
        testPhone.setColors(new HashSet<>(colors));
        List<Long> colorIds = jdbcColorDao.saveAll(colors);
        Long phoneId = phoneDao.save(testPhone);
        phoneDao.savePhoneColorRelations(colorIds, phoneId);
        Long savedRelationsNumber = jdbcTemplate.queryForObject(verifyingSql, Long.class, phoneId);
        assertEquals(testPhone.getColors().size(), savedRelationsNumber);
    }


}
