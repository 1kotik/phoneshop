package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.model.B2BCartForm;
import com.es.phoneshop.web.utils.ParameterExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart/b2b")
public class B2BCartPage {
    private final CartService cartService;

    @Autowired
    public B2BCartPage(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String getB2BCartPage(Model model) {
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartService.getCartTotals());
        model.addAttribute("b2bCartForm", new B2BCartForm());
        return AppConstants.Pages.B2B_CART;
    }

    @PostMapping
    public String addItems(
            @ModelAttribute @Valid B2BCartForm b2bCartForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        Map<String, Integer> items = collectItemsFromB2BCartForm(b2bCartForm, request);
        Map<String, String> validationErrors = extractErrors(bindingResult, items);
        Map<String, String> insertErrors = cartService.b2bInsert(items);
        validationErrors.putAll(insertErrors);
        if (!validationErrors.isEmpty()) {
            redirectAttributes.addFlashAttribute("b2bCartFormErrors", validationErrors);
            redirectAttributes.addFlashAttribute("b2bCartForm", b2bCartForm);
        }
        return AppConstants.Pages.REDIRECT_B2B_CART;
    }

    private Map<String, Integer> collectItemsFromB2BCartForm(B2BCartForm b2bCartForm, HttpServletRequest request) {
        int rowNum = Integer.parseInt(request.getParameter("rowNum"));
        Map<String, Integer> formItems = b2bCartForm.getItems();
        Map<String, Integer> items = new HashMap<>();
        for (int i = 0; i < rowNum; i++) {
            String productModelParameterName = String.format("model%d", i);
            String productModel = request.getParameter(productModelParameterName);
            Integer quantity = formItems.get(productModelParameterName);
            if (quantity != null) {
                items.put(productModel, quantity);
            }
        }
        return items;
    }

    private Map<String, String> extractErrors(BindingResult bindingResult, Map<String, Integer> items) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String itemCode = ParameterExtractor.extractItemCodeFromBindingResultField(fieldError.getField());
            validationErrors.put(itemCode, AppConstants.ErrorMessages.INVALID_FORMAT);
            items.remove(itemCode);
        }
        return validationErrors;
    }
}
