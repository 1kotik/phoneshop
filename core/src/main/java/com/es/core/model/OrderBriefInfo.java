package com.es.core.model;

import com.es.core.enums.OrderStatus;
import com.es.core.util.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderBriefInfo {
    private Long id;
    private String contactPhoneNo;
    private String deliveryAddress;
    private LocalDateTime dateOfRegistration;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String dateOfRegistrationFormatted;
    private String customerFirstName;
    private String customerLastName;

    public OrderBriefInfo() {}

    public OrderBriefInfo(Long id, String contactPhoneNo, String deliveryAddress, LocalDateTime dateOfRegistration,
                          BigDecimal totalPrice, OrderStatus status,
                          String customerFirstName, String customerLastName) {
        this.id = id;
        this.contactPhoneNo = contactPhoneNo;
        this.deliveryAddress = deliveryAddress;
        this.dateOfRegistration = dateOfRegistration;
        this.totalPrice = totalPrice;
        this.status = status;
        this.dateOfRegistrationFormatted = dateOfRegistration.format(AppConstants.DefaultConstants.DATE_TIME_FORMATTER);
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDateTime dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
        this.dateOfRegistrationFormatted = dateOfRegistration.format(AppConstants.DefaultConstants.DATE_TIME_FORMATTER);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDateOfRegistrationFormatted() {
        return dateOfRegistrationFormatted;
    }

    public void setDateOfRegistrationFormatted(String dateOfRegistrationFormatted) {
        this.dateOfRegistrationFormatted = dateOfRegistrationFormatted;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }
}
