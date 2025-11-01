package com.es.phoneshop.web.controller.ajax;

import com.es.core.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ajaxAdminOrder")
public class AjaxAdminOrderPageController {
    @Resource
    private OrderService orderService;

    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(
            @PathVariable(name = "orderId") Long orderId,
            @RequestParam(name = "newStatus") String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
    }
}
