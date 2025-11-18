package com.es.phoneshop.web.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

public class B2BCartForm {
    private Map<@NotBlank String, @Min(1) @NotNull Integer> items = new HashMap<>();

    public B2BCartForm() {

    }

    public B2BCartForm(Map<@NotBlank String, @Min(1) @NotNull Integer> items) {
        this.items = items;
    }

    public Map<@NotBlank String, @Min(1) @NotNull Integer> getItems() {
        return items;
    }

    public void setItems(Map<@NotBlank String, @Min(1) @NotNull Integer> items) {
        this.items = items;
    }
}
