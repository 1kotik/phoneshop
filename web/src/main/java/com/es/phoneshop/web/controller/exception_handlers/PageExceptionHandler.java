package com.es.phoneshop.web.controller.exception_handlers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.es.phoneshop.web.controller.pages")
public class PageExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public String handleOtherExceptions(Throwable e, Model model) {
        model.addAttribute("message", "Sorry! Something went wrong");
        return "error";
    }
}
