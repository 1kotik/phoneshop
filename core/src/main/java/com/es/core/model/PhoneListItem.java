package com.es.core.model;

import java.math.BigDecimal;
import java.util.Set;

public class PhoneListItem {
    private Long id;
    private String brand;
    private String model;
    private BigDecimal price;
    private BigDecimal displaySizeInches;
    private String imageUrl;
    private Set<Color> colors;

    public PhoneListItem() {
    }

    public PhoneListItem(Long id, String model, String brand, BigDecimal price, BigDecimal displaySizeInches,
                         String imageUrl, Set<Color> colors) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.price = price;
        this.displaySizeInches = displaySizeInches;
        this.imageUrl = imageUrl;
        this.colors = colors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Color> getColors() {
        return colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getDisplaySizeInches() {
        return displaySizeInches;
    }

    public void setDisplaySizeInches(BigDecimal displaySizeInches) {
        this.displaySizeInches = displaySizeInches;
    }
}
