package com.es.phoneshop.web.controller.pages;

import com.es.core.util.AppConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @GetMapping("/{orderId}")
    public String orderOverviewPage(@PathVariable("orderId") UUID orderId, Model model) {
        return AppConstants.Pages.ORDER_OVERVIEW;
    }
}
