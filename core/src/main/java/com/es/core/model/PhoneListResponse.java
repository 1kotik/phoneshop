package com.es.core.model;

import java.util.List;

public class PhoneListResponse {
    private List<PhoneListItem> phones;
    private int totalPages;

    public PhoneListResponse(List<PhoneListItem> phones, int totalPages) {
        this.phones = phones;
        this.totalPages = totalPages;
    }

    public PhoneListResponse() {
    }


    public List<PhoneListItem> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneListItem> phones) {
        this.phones = phones;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
