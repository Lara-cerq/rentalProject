package com.rentaloc.controllers;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import com.rentaloc.models.Messages;
import com.rentaloc.models.Rentals;
import com.rentaloc.services.RentalsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/api/rentals")
public class RentalsController {

    @Autowired
    RentalsService rentalsService;

    @GetMapping("")
    public Iterable<Rentals> getAllRentals() {
        return rentalsService.getAllRentals();
    }

    @GetMapping("/{id}")
    public Optional<Rentals> getRentalById(@PathVariable("id") Integer id) {
        return rentalsService.getById(id);
    }

    @PostMapping("/{id}")
    public Rentals saveRentals(@PathVariable(required = true, name = "id") Integer id, @Valid Rentals rental) {
        return rentalsService.addRental(rental);
    }

    @PutMapping("/{id}")
    public Rentals updateRentals(@PathVariable(required = true, name = "id") Integer id, @RequestBody Rentals rentalDetails) {
        Rentals updateRental = rentalsService.getById(id).get();
        updateRental.setName(rentalDetails.getName());
        updateRental.setDescription(rentalDetails.getDescription());
        updateRental.setPicture(rentalDetails.getPicture());
        updateRental.setPrice(rentalDetails.getPrice());
        updateRental.setSurface(rentalDetails.getSurface());

        rentalsService.updateRental(updateRental);
        return updateRental;
    }
}
