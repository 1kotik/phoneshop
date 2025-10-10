package com.es.core.model;

public class ColorWithPhoneId {
    private Long id;
    private String code;
    private Long phoneId;

    public ColorWithPhoneId() {
    }

    public ColorWithPhoneId(Long id, String code, Long phoneId) {
        this.id = id;
        this.code = code;
        this.phoneId = phoneId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Long phoneId) {
        this.phoneId = phoneId;
    }
}
