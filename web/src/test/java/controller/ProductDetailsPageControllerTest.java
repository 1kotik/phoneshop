package controller;

import com.es.core.model.CartTotals;
import com.es.core.model.Phone;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.ProductDetailsPageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDetailsPageControllerTest {
    @Mock
    private PhoneService phoneService;
    @Mock
    private CartService cartService;
    @InjectMocks
    private ProductDetailsPageController productDetailsPageController;

    @Test
    void shouldShowProductDetailsPage() {
        Long phoneId = 1L;
        Model model = new ExtendedModelMap();
        CartTotals cartTotals = new CartTotals(100, BigDecimal.TEN);
        Phone phone = new Phone();
        phone.setId(phoneId);
        phone.setModel("model");
        when(phoneService.get(phoneId)).thenReturn(phone);
        when(cartService.getCartTotals()).thenReturn(cartTotals);
        String view = productDetailsPageController.showProductDetails(phoneId, model);
        assertEquals(AppConstants.Pages.PRODUCT_DETAILS, view);
        assertEquals(cartTotals, model.getAttribute(AppConstants.PageAttributes.CART_TOTALS));
        assertEquals(phone, model.getAttribute(AppConstants.PageAttributes.PHONE));
    }
}
