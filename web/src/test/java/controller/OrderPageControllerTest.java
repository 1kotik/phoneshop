package controller;

import com.es.core.model.Cart;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.OrderPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPageControllerTest {
    @Mock
    private OrderService orderService;
    @Mock
    private Cart cart;
    @InjectMocks
    private OrderPageController orderPageController;

    @Test
    void shouldGetOrderPage() {
        Order order = new Order();
        Model model = new ExtendedModelMap();
        when(orderService.createOrder()).thenReturn(order);
        String view = orderPageController.getOrder(model);
        assertEquals(AppConstants.Pages.ORDER, view);
        assertEquals(model.getAttribute(AppConstants.PageAttributes.ORDER), order);
    }

    @Test
    void shouldPlaceOrderAndRedirectToOverviewPage() {
        OrderCustomerInfo customerInfo = new OrderCustomerInfo();
        BindingResult bindingResult = new BeanPropertyBindingResult(customerInfo,
                AppConstants.PageAttributes.ORDER_CUSTOMER_INFO);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        SessionStatus sessionStatus = new SimpleSessionStatus();
        Order order = new Order();
        order.setSecureId(UUID.randomUUID());
        when(orderService.placeOrder(order, customerInfo)).thenReturn(order);
        String view = orderPageController.placeOrder(customerInfo, bindingResult, redirectAttributes,
                order, sessionStatus);
        assertEquals(String.format("%s/%s", AppConstants.Pages.REDIRECT_ORDER_OVERVIEW, order.getSecureId()), view);
        assertEquals(order, redirectAttributes.getFlashAttributes().get(AppConstants.PageAttributes.ORDER));
        assertEquals(1, redirectAttributes.getFlashAttributes().size());
    }

    @Test
    void shouldFindErrorsAndRedirectToOrderPageWhenPlacingOrder() {
        OrderCustomerInfo customerInfo = new OrderCustomerInfo();
        BindingResult bindingResult = new BeanPropertyBindingResult(customerInfo,
                AppConstants.PageAttributes.ORDER_CUSTOMER_INFO);
        SessionStatus sessionStatus = new SimpleSessionStatus();
        Order order = new Order();
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        bindingResult.addError(new ObjectError("firstName", "error"));
        String view = orderPageController.placeOrder(customerInfo, bindingResult, redirectAttributes,
                order, sessionStatus);
        assertEquals(AppConstants.Pages.REDIRECT_ORDER, view);
        assertEquals(customerInfo, redirectAttributes.getFlashAttributes()
                .get(AppConstants.PageAttributes.ORDER_CUSTOMER_INFO));
        assertEquals(2, redirectAttributes.getFlashAttributes().size());
    }
}
