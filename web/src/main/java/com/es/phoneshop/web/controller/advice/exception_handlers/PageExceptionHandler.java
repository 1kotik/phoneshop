package com.es.phoneshop.web.controller.advice.exception_handlers;

import com.es.core.util.AppConstants;
import com.es.core.util.LogMessageCreator;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.es.phoneshop.web.controller.pages")
public class PageExceptionHandler {
    private final Logger logger = Logger.getLogger(this.getClass());

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOtherExceptions(Throwable e, Model model) {
        logger.warn(LogMessageCreator.createExceptionMessage(e));
        model.addAttribute(AppConstants.PageAttributes.ERROR, AppConstants.ErrorMessages.INTERNAL_ERROR);
        return AppConstants.Pages.ERROR;
    }
}
