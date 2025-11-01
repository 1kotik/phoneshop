package controller;

import com.es.core.model.Order;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.admin.OrdersPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrdersPageControllerTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrdersPageController ordersPageController;

    @Test
    void shouldGetOrdersPage() {
        Model model = new ExtendedModelMap();
        when(orderService.findAll()).thenReturn(new ArrayList<>());
        String view = ordersPageController.ordersPage(model);
        assertEquals(AppConstants.Pages.ADMIN_ORDERS, view);
        assertTrue(model.containsAttribute(AppConstants.PageAttributes.ADMIN_ORDERS));
    }

    @Test
    void shouldGetOrderPage() {
        Long orderId = 1L;
        Order order = new Order();
        Model model = new ExtendedModelMap();
        when(orderService.findById(orderId)).thenReturn(order);
        String view = ordersPageController.orderPage(orderId, model);
        assertEquals(AppConstants.Pages.ADMIN_ORDER_OVERVIEW, view);
        assertTrue(model.containsAttribute(AppConstants.PageAttributes.ORDER));
    }
}
