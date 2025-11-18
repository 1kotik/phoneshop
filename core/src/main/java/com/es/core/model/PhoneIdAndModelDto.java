package com.es.core.model;

public class PhoneIdAndModelDto {
    private Long id;
    private String model;

    public PhoneIdAndModelDto() {

    }

    public PhoneIdAndModelDto(Long id, String model) {
        this.id = id;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
