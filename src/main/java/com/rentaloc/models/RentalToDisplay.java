package com.rentaloc.models;

import jakarta.validation.constraints.Size;

import java.util.Date;

public class RentalToDisplay {

    private Integer id;

    private String name;

    private Double surface;

    private Double price;

    private String picture;

    @Size(max = 65555)
    private String description;

    private Integer owner_id;

    private Date created_at;

    private Date updated_at;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getSurface() {
        return surface;
    }

    public Double getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public RentalToDisplay() {
        super();
    }


    public RentalToDisplay(Integer id, String name, Double surface, Double price, String picture, String description, Integer owner_id, Date created_at, Date updated_at) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner_id = owner_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "{" +
                "  id:" + id +
                ", name:'" + name + '\'' +
                ", surface:" + surface +
                ", price:" + price +
                ", picture:'" + picture + '\'' +
                ", description:'" + description + '\'' +
                ", owner_id:" + owner_id +
                ", created_at:" + created_at +
                ", updated_at:" + updated_at +
                '}';
    }
}
