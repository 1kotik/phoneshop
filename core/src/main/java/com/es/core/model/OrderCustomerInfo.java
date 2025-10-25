package com.es.core.model;

import com.es.core.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;

public class OrderCustomerInfo {
    @NotBlank(message = "Provide your first name")
    private String firstName;
    @NotBlank(message = "Provide your last name")
    private String lastName;
    @NotBlank(message = "Provide delivery address")
    private String deliveryAddress;
    @NotBlank(message = "Provide contact phone number")
    @PhoneNumber
    private String contactPhoneNo;
    private String additionalInformation;

    public OrderCustomerInfo() {
    }

    public OrderCustomerInfo(String firstName, String lastName, String deliveryAddress, String contactPhoneNo,
                             String additionalInformation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.deliveryAddress = deliveryAddress;
        this.contactPhoneNo = contactPhoneNo;
        this.additionalInformation = additionalInformation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
