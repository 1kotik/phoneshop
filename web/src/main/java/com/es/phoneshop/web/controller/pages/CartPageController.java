package com.es.phoneshop.web.controller.pages;

import com.es.core.model.Cart;
import com.es.core.model.ErrorItem;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.model.CartUpdateForm;
import com.es.phoneshop.web.utils.ParameterExtractor;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        CartUpdateForm cartUpdateForm = new CartUpdateForm(cart);
        model.addAttribute(AppConstants.PageAttributes.CART_UPDATE_FORM, cartUpdateForm);
        model.addAttribute(AppConstants.PageAttributes.CART, cart);
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartService.getCartTotals());
        return AppConstants.Pages.CART;
    }

    @PutMapping
    public String updateCart(
            @ModelAttribute(AppConstants.PageAttributes.CART_UPDATE_FORM) @Valid CartUpdateForm cartUpdateForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        Map<Long, ErrorItem> validationErrors = extractErrors(bindingResult, cartUpdateForm.getItems());
        Map<Long, ErrorItem> cartServiceErrors = cartService.update(cartUpdateForm.getItems());
        validationErrors.putAll(cartServiceErrors);
        redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.CART_UPDATE_ERRORS, validationErrors);
        return AppConstants.Pages.REDIRECT_CART;
    }

    private Map<Long, ErrorItem> extractErrors(BindingResult bindingResult, Map<Long, Integer> items) {
        Map<Long, ErrorItem> validationErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            Long itemId = ParameterExtractor.extractItemIdFromBindingResultField(fieldError.getField());
            validationErrors.put(itemId,
                    new ErrorItem(fieldError.getRejectedValue(), AppConstants.ErrorMessages.INVALID_FORMAT));
            items.remove(itemId);
        }
        return validationErrors;
    }
}
