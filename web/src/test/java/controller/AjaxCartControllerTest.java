package controller;

import com.es.core.model.CartTotals;
import com.es.core.service.CartService;
import com.es.phoneshop.web.controller.ajax.AjaxCartController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AjaxCartControllerTest {
    @Mock
    private CartService cartService;
    @InjectMocks
    private AjaxCartController ajaxCartController;

    @Test
    void shouldAddPhoneToCart() {
        Long phoneId = 1L;
        Integer quantity = 2;
        CartTotals cartTotals = new CartTotals(12, BigDecimal.TEN);
        doNothing().when(cartService).addPhone(phoneId, quantity);
        when(cartService.getCartTotals()).thenReturn(cartTotals);
        CartTotals response = ajaxCartController.addPhone(phoneId, quantity);
        assertEquals(cartTotals.getTotalQuantity(), response.getTotalQuantity());
        assertEquals(cartTotals.getTotalPrice(), response.getTotalPrice());
    }

    @Test
    void shouldRemovePhoneFromCart() {
        Long phoneId = 1L;
        CartTotals cartTotals = new CartTotals(12, BigDecimal.TEN);
        doNothing().when(cartService).remove(phoneId);
        when(cartService.getCartTotals()).thenReturn(cartTotals);
        CartTotals response = ajaxCartController.removePhone(phoneId);
        assertEquals(cartTotals.getTotalQuantity(), response.getTotalQuantity());
        assertEquals(cartTotals.getTotalPrice(), response.getTotalPrice());
    }
}
