package com.es.phoneshop.web.controller.pages;


import com.es.core.model.CartTotals;
import com.es.core.model.PhoneListResponse;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.util.AppConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @GetMapping
    public String showProductList(
            Model model,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "criteria", defaultValue = "") String sortCriteria,
            @RequestParam(name = "order", defaultValue = "") String sortOrder,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "itemsPerPage", defaultValue = "10") int phonesPerPage) {
        PhoneListResponse response = phoneService.findAll(query, sortCriteria, sortOrder, page, phonesPerPage);
        CartTotals cartTotals = cartService.getCartTotals();
        model.addAttribute(AppConstants.PageAttributes.PHONE_LIST_RESPONSE, response);
        model.addAttribute(AppConstants.PageAttributes.CART_TOTALS, cartTotals);
        return AppConstants.Pages.PRODUCT_LIST;
    }
}
