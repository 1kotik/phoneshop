package com.es.core.model;

public class Color {
    private Long id;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Color(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public Color() {

    }
}
