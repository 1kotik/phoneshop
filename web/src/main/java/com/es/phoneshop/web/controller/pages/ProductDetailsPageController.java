package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.util.AppConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/productDetails")
public class ProductDetailsPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @GetMapping("/{productId}")
    public String showProductDetails(@PathVariable("productId") Long productId, Model model) {
        model.addAttribute(AppConstants.PageAttributes.PHONE, phoneService.get(productId));
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartService.getCartTotals());
        return AppConstants.Pages.PRODUCT_DETAILS;
    }
}
