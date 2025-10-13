package util;

import com.es.core.model.CartItem;
import com.es.core.model.Color;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import com.es.core.util.SqlUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PhoneTestUtils {
    private PhoneTestUtils() {
    }

    public static Phone getPhone(Long id) {
        String str = "test";
        BigDecimal decimal = new BigDecimal("1.0");
        Phone phone = new Phone();
        phone.setId(id);
        phone.setBrand(str);
        phone.setModel(str);
        phone.setPrice(decimal);

        return phone;
    }

    public static Color createColor() {
        Color color = new Color();
        color.setId(1L);
        color.setCode("");

        return color;
    }

    public static void loadTestData(JdbcTemplate jdbcTemplate) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Color.TABLE_NAME);
            JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Phone.TABLE_NAME);
            JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Stock.TABLE_NAME);
            JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Phone.PHONES_COLORS_RELATIONS_TABLE_NAME);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("db/test-demodata.sql"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PhoneListItem> getPhoneList() {
        PhoneListItem item1 = new PhoneListItem(1L, "brand1", "model1",
                BigDecimal.TEN, BigDecimal.TEN, "image1", Set.of(createColor()));
        PhoneListItem item2 = new PhoneListItem(2L, "brand2", "model2",
                BigDecimal.TEN, BigDecimal.TEN, "image2", Set.of(createColor()));
        return List.of(item1, item2);
    }

    public static List<CartItem> getCartList() {
        CartItem cartItem1 = new CartItem(getPhone(1L), 2);
        CartItem cartItem2 = new CartItem(getPhone(2L), 2);
        return new ArrayList<>(List.of(cartItem1, cartItem2));
    }

}
