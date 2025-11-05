package com.es.phoneshop.web.controller.exception_handlers;

import com.es.core.util.AppConstants;
import com.es.core.util.LogMessageCreator;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.es.phoneshop.web.controller.pages")
public class PageExceptionHandler {
    private final Logger logger = Logger.getLogger(this.getClass());
    @ExceptionHandler(Throwable.class)
    public String handleOtherExceptions(Throwable e, Model model) {
        logger.warn(LogMessageCreator.createExceptionMessage(e));
        model.addAttribute(AppConstants.PageAttributes.ERROR, AppConstants.ErrorMessages.INTERNAL_ERROR);
        return AppConstants.Pages.ERROR;
    }
}
