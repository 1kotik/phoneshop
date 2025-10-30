package controller;

import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.OrderOverviewPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderOverviewPageControllerTest {
    @InjectMocks
    private OrderOverviewPageController orderOverviewPageController;

    @Test
    void shouldGetOrderOverviewPage() {
        String view = orderOverviewPageController.orderOverviewPage(UUID.randomUUID(), new ExtendedModelMap());
        assertEquals(AppConstants.Pages.ORDER_OVERVIEW, view);
    }
}
