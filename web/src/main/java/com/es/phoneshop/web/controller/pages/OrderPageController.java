package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.InconsistentOrderException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.Cart;
import com.es.core.model.Order;
import com.es.core.model.OrderCustomerInfo;
import com.es.core.service.OrderService;
import com.es.core.util.AppConstants;
import com.es.core.util.LogMessageCreator;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
@SessionAttributes(AppConstants.PageAttributes.ORDER)
public class OrderPageController {
    @Resource(name = "defaultOrderService")
    private OrderService orderService;
    @Resource
    private Cart cart;
    private static final Logger logger = LoggerFactory.getLogger(OrderPageController.class);

    @GetMapping
    public String getOrder(Model model) throws OutOfStockException {
        Order order = orderService.createOrder();
        OrderCustomerInfo customerInfo = (OrderCustomerInfo) model
                .getAttribute(AppConstants.PageAttributes.ORDER_CUSTOMER_INFO);
        if (customerInfo == null) {
            model.addAttribute(AppConstants.PageAttributes.ORDER_CUSTOMER_INFO, new OrderCustomerInfo());
        }
        model.addAttribute(AppConstants.PageAttributes.ORDER, order);
        return AppConstants.Pages.ORDER;
    }

    @PostMapping
    public String placeOrder(
            @ModelAttribute(AppConstants.PageAttributes.ORDER_CUSTOMER_INFO) @Valid OrderCustomerInfo customerInfo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @ModelAttribute(AppConstants.PageAttributes.ORDER) Order order,
            SessionStatus sessionStatus) throws OutOfStockException {
        if (bindingResult.hasErrors()) {
            Map<String, String> validationErrors = extractErrors(bindingResult);
            redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.VALIDATION_ERRORS, validationErrors);
            redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.ORDER_CUSTOMER_INFO, customerInfo);
            return AppConstants.Pages.REDIRECT_ORDER;
        }
        Order placedOrder = orderService.placeOrder(order, customerInfo);
        redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.ORDER, placedOrder);
        sessionStatus.setComplete();
        return String.format("%s/%s", AppConstants.Pages.REDIRECT_ORDER_OVERVIEW, placedOrder.getSecureId());
    }

    private Map<String, String> extractErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        logger.warn(LogMessageCreator.createExceptionMessage(e, OrderPageController.class));
        model.addAttribute(
                AppConstants.PageAttributes.ERROR,
                AppConstants.ErrorMessages.ORDER_HAS_ALREADY_BEEN_PLACED);
        return AppConstants.Pages.ERROR;
    }

    @ExceptionHandler(OutOfStockException.class)
    public String handleOutOfStockException(OutOfStockException e, RedirectAttributes redirectAttributes) {
        logger.warn(LogMessageCreator.createExceptionMessage(e, OrderPageController.class));
        redirectAttributes.addFlashAttribute(AppConstants.PageAttributes.ERROR,
                AppConstants.ErrorMessages.CART_ITEMS_OUT_OF_STOCK);
        return AppConstants.Pages.REDIRECT_ORDER;
    }

    @ExceptionHandler(InconsistentOrderException.class)
    public String handleInconsistentOrderException(InconsistentOrderException e,
                                                   RedirectAttributes redirectAttributes) {
        logger.warn(LogMessageCreator.createExceptionMessage(e, OrderPageController.class));
        redirectAttributes.addFlashAttribute(
                AppConstants.PageAttributes.ERROR,
                e.getMessage());
        return AppConstants.Pages.REDIRECT_ORDER;
    }
}
