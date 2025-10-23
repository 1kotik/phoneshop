package controller;

import com.es.core.model.Cart;
import com.es.core.model.CartTotals;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.controller.pages.CartPageController;
import com.es.phoneshop.web.model.CartUpdateForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartPageControllerTest {
    @Mock
    private CartService cartService;
    @InjectMocks
    private CartPageController cartPageController;

    @Test
    void shouldShowCartPage() {
        Model model = new ExtendedModelMap();
        CartTotals cartTotals = new CartTotals(100, BigDecimal.TEN);
        Cart cart = new Cart();
        when(cartService.getCart()).thenReturn(cart);
        when(cartService.getCartTotals()).thenReturn(cartTotals);
        String view = cartPageController.getCart(model);
        assertEquals(model.getAttribute(AppConstants.PageAttributes.CART), cart);
        assertEquals(model.getAttribute(AppConstants.PageAttributes.CART_TOTALS), cartTotals);
        assertEquals(AppConstants.Pages.CART, view);
    }

    @Test
    void shouldUpdateItemsAndRedirectToCartPage() {
        CartUpdateForm cartUpdateForm = new CartUpdateForm(Map.of(1L, 1, 2L, 2));
        BindingResult bindingResult = new BeanPropertyBindingResult(
                cartUpdateForm, AppConstants.PageAttributes.CART_UPDATE_FORM);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        when(cartService.update(cartUpdateForm.getItems())).thenReturn(Collections.emptyMap());
        String view = cartPageController.updateCart(cartUpdateForm, bindingResult, redirectAttributes);
        assertEquals(AppConstants.Pages.REDIRECT_CART, view);
    }

    @Test
    void shouldExtractErrorsAndUpdateItemsAndRedirectToCartPage() {
        Map<Long, Integer> items = new HashMap<>(Map.of(1L, 0, 2L, 2));
        CartUpdateForm cartUpdateForm = new CartUpdateForm(items);
        BindingResult bindingResult = new BeanPropertyBindingResult(
                cartUpdateForm, AppConstants.PageAttributes.CART_UPDATE_FORM);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        bindingResult.addError(new FieldError(AppConstants.PageAttributes.CART_UPDATE_FORM,
                "items[1]", "error"));
        when(cartService.update(cartUpdateForm.getItems())).thenReturn(Collections.emptyMap());
        String view = cartPageController.updateCart(cartUpdateForm, bindingResult, redirectAttributes);
        assertEquals(AppConstants.Pages.REDIRECT_CART, view);
    }

}
