package com.rentaloc.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(
            cascade=CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName="id")
    private Users users;

    @ManyToOne(
            cascade=CascadeType.PERSIST)
    @JoinColumn(name = "rental_id", referencedColumnName="id")
    private Rentals rentals;

    private String message;

    private Timestamp created_at;

    private Timestamp updated_at;

    public Messages() {
    }

    public Messages(Integer id, String message, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.message = message;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Users getUsers() {
        return users;
    }

    public Rentals getRentals() {
        return rentals;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public void setRentals(Rentals rentals) {
        this.rentals = rentals;
    }
}
