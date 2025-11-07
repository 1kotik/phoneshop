package com.es.phoneshop.web.utils;

import com.es.core.enums.OrderStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToOrderStatusConverter implements Converter<String, OrderStatus> {
    @Override
    public OrderStatus convert(String source) {
        return OrderStatus.getOrderStatus(source);
    }
}
