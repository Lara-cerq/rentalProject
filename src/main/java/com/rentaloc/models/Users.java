package com.rentaloc.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

    private String password;

    private Timestamp created_at;

    private Timestamp updated_at;

    @OneToMany(mappedBy = "users")
    List<Rentals> rentalsList=new ArrayList<>();

    @OneToMany (mappedBy = "users")
    List<Messages> messagesList=new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Users() {
    }

    public Users(Integer id, String email, String name, String password, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Users(Integer id, String email, String name, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
