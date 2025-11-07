package controller;

import com.es.core.model.Order;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.OrderOverviewPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderOverviewPageControllerTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderOverviewPageController orderOverviewPageController;

    @Test
    void shouldGetOrderOverviewPageWhenOrderAttributeIsNotPresent() {
        UUID secureId = UUID.randomUUID();
        Model model = new ExtendedModelMap();
        when(orderService.findBySecureId(secureId)).thenReturn(new Order());
        String view = orderOverviewPageController.orderOverviewPage(secureId, model);
        assertTrue(model.containsAttribute(AppConstants.PageAttributes.ORDER));
        assertEquals(AppConstants.Pages.ORDER_OVERVIEW, view);
    }

    @Test
    void shouldGetOrderOverviewPageWhenOrderAttributeIsPresent() {
        UUID secureId = UUID.randomUUID();
        Model model = new ExtendedModelMap();
        model.addAttribute(AppConstants.PageAttributes.ORDER, new Order());
        String view = orderOverviewPageController.orderOverviewPage(secureId, model);
        assertTrue(model.containsAttribute(AppConstants.PageAttributes.ORDER));
        assertEquals(AppConstants.Pages.ORDER_OVERVIEW, view);
    }

}
