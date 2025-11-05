package controller;

import com.es.core.enums.OrderStatus;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.controller.ajax.AjaxAdminOrderPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class AjaxAdminOrderPageControllerTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private AjaxAdminOrderPageController ajaxAdminOrderPageController;

    @Test
    void shouldUpdateOrderStatus() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.DELIVERED;
        doNothing().when(orderService).updateOrderStatus(orderId, newStatus);
        assertDoesNotThrow(() -> ajaxAdminOrderPageController.updateOrderStatus(orderId, newStatus));
    }
}
