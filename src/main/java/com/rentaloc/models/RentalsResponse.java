package com.rentaloc.models;

import java.util.List;

public class RentalsResponse {

    private List<Rentals> rentals;

    public List<Rentals> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rentals> rentals) {
        this.rentals = rentals;
    }

    public RentalsResponse(List<Rentals> rentals) {
        this.rentals = rentals;
    }
}
