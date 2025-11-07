package util;

import com.es.core.enums.OrderStatus;
import com.es.core.model.CartItem;
import com.es.core.model.Color;
import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.model.OrderItem;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import com.es.core.model.Stock;
import com.es.core.util.SqlUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
            JdbcTestUtils.deleteFromTables(jdbcTemplate, SqlUtils.Order.TABLE_NAME);
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
        List<PhoneListItem> getPhoneList = getPhoneList();
        CartItem cartItem1 = new CartItem(getPhoneList.get(0), 2);
        CartItem cartItem2 = new CartItem(getPhoneList.get(1), 2);
        return new ArrayList<>(List.of(cartItem1, cartItem2));
    }

    public static List<Stock> getStockList() {
        return new ArrayList<>(List.of(
                new Stock(1L, 10, 1),
                new Stock(2L, 10, 1)));
    }

    public static OrderCustomerInfo getOrderCustomerInfo() {
        OrderCustomerInfo customerInfo = new OrderCustomerInfo();
        customerInfo.setContactPhoneNo("+111111111111");
        customerInfo.setAdditionalInformation("info");
        customerInfo.setDeliveryAddress("address");
        customerInfo.setFirstName("firstname");
        customerInfo.setLastName("lastname");
        return customerInfo;
    }

    public static Order getOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setSecureId(UUID.randomUUID());
        order.setOrderItems(getCartList().stream().map(OrderItem::new).toList());
        order.setDeliveryPrice(BigDecimal.ONE);
        order.setStatus(OrderStatus.NEW);
        order.setTotalPrice(BigDecimal.TEN.add(BigDecimal.ONE));
        order.setCustomerInfo(getOrderCustomerInfo());
        order.setSubtotal(BigDecimal.TEN);
        return order;
    }

    public static OrderBriefInfo getOrderBriefInfo(Long id) {
        OrderBriefInfo order = new OrderBriefInfo();
        order.setId(id);
        order.setContactPhoneNo("+111111111111");
        order.setDeliveryAddress("address");
        order.setDateOfRegistration(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.TEN);
        order.setStatus(OrderStatus.NEW);
        order.setCustomerFirstName("firstname");
        order.setCustomerLastName("lastname");
        return order;
    }

    public static List<OrderBriefInfo> getOrderBriefInfoList() {
        return List.of(getOrderBriefInfo(1L), getOrderBriefInfo(2L));
    }

}
