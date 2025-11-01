package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.model.Order;
import com.es.core.model.OrderBriefInfo;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/orders")
public class OrdersPageController {
    @Resource
    private OrderService orderService;
    @GetMapping
    public String ordersPage(Model model) {
        List<OrderBriefInfo> orders = orderService.findAll();
        model.addAttribute(AppConstants.PageAttributes.ADMIN_ORDERS, orders);
        return AppConstants.Pages.ADMIN_ORDERS;
    }

    @GetMapping("/{orderId}")
    public String orderPage(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute(AppConstants.PageAttributes.ORDER, order);
        return AppConstants.Pages.ADMIN_ORDER_OVERVIEW;
    }
}
