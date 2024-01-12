package com.rentaloc.models;

import org.springframework.web.multipart.MultipartFile;

public class RentalsForUpdateFormData {

    private Integer id;

    private String name;

    private Double surface;

    private Double price;

    private String description;

    public RentalsForUpdateFormData() {
    }

    public RentalsForUpdateFormData(Integer id, String name, Double surface, Double price, String description) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
