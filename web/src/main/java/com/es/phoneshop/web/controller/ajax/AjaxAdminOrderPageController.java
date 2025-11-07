package com.es.phoneshop.web.controller.ajax;

import com.es.core.enums.OrderStatus;
import com.es.core.service.OrderService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
public class AjaxAdminOrderPageController {
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AjaxAdminOrderPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PatchMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(
            @PathVariable(name = "orderId") Long orderId,
            @RequestParam(name = "newStatus") OrderStatus newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        logger.info("Order {} status has updated to '{}'", orderId, newStatus);
    }
}
