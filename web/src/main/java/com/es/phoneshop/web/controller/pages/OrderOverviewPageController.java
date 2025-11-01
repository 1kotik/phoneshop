package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.Order;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public String orderOverviewPage(@PathVariable("orderId") UUID orderId, Model model) {
        if(!model.containsAttribute(AppConstants.PageAttributes.ORDER)) {
            Order order = orderService.findBySecureId(orderId);
            model.addAttribute(AppConstants.PageAttributes.ORDER, order);
        }
        return AppConstants.Pages.ORDER_OVERVIEW;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFoundException(OrderNotFoundException e, Model model) {
        model.addAttribute(AppConstants.PageAttributes.ERROR, e.getMessage());
        return AppConstants.Pages.ERROR;
    }
}
