package com.rentaloc.models;

import java.util.List;

public class RentalsResponse {

    private List<RentalToDisplay> rentals;

    public List<RentalToDisplay> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalToDisplay> rentals) {
        this.rentals = rentals;
    }
}
