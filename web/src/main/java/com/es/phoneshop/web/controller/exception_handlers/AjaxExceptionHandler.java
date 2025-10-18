package com.es.phoneshop.web.controller.exception_handlers;

import com.es.core.exception.OutOfStockException;
import com.es.core.util.AppConstants;
import com.es.phoneshop.web.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "com.es.phoneshop.web.controller.ajax")
public class AjaxExceptionHandler {

    @ExceptionHandler(exception = {MethodArgumentTypeMismatchException.class, HandlerMethodValidationException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException() {
        return new ErrorResponse(400, AppConstants.ErrorMessages.INVALID_FORMAT);
    }

    @ExceptionHandler(OutOfStockException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOutOfStockException(OutOfStockException e) {
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(Throwable e) {
        return new ErrorResponse(500, AppConstants.ErrorMessages.INTERNAL_ERROR);
    }
}
