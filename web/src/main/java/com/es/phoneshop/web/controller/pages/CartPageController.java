package com.es.phoneshop.web.controller.pages;

import com.es.core.model.ErrorItem;
import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.model.CartUpdateForm;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    private Map<Long, ErrorItem> updateErrors = new HashMap<>();

    @GetMapping
    public String getCart(Model model) {
        CartUpdateForm cartUpdateForm = new CartUpdateForm(cartService.getCart());
        model.addAttribute(AppConstants.PageAttributes.CART_UPDATE_FORM, cartUpdateForm);
        model.addAttribute(AppConstants.PageAttributes.CART, cartService.getCart());
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartService.getCartTotals());
        model.addAttribute(AppConstants.PageAttributes.CART_UPDATE_ERRORS, new HashMap<>(updateErrors));
        updateErrors.clear();
        return AppConstants.Pages.CART;
    }

    @PutMapping
    public String updateCart(
            @ModelAttribute(AppConstants.PageAttributes.CART_UPDATE_FORM) @Valid CartUpdateForm cartUpdateForm,
            BindingResult bindingResult) {
        extractErrors(bindingResult, updateErrors, cartUpdateForm.getItems());
        Map<Long, ErrorItem> cartServiceErrors = cartService.update(cartUpdateForm.getItems());
        updateErrors.putAll(cartServiceErrors);
        return AppConstants.Pages.REDIRECT_CART;
    }

    private void extractErrors(BindingResult bindingResult, Map<Long, ErrorItem> validationErrors,
                               Map<Long, Integer> items) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            Long itemId = extractItemIdFromBindingResultField(fieldError.getField());
            validationErrors.put(itemId,
                    new ErrorItem(fieldError.getRejectedValue(), AppConstants.ErrorMessages.INVALID_FORMAT));
            items.remove(itemId);
        }
    }

    private Long extractItemIdFromBindingResultField(String fieldName) {
        Pattern pattern = Pattern.compile("items\\[(\\d+)]");
        Matcher matcher = pattern.matcher(fieldName);
        return matcher.find() ? Long.valueOf(matcher.group(1)) : null;
    }
}
