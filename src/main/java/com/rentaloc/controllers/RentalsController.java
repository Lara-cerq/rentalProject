package com.rentaloc.controllers;

import com.rentaloc.models.Messages;
import com.rentaloc.models.Rentals;
import com.rentaloc.services.RentalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api")
public class RentalsController {

    @Autowired
    RentalsService rentalsService;

    @GetMapping("/rentals")
    List<Rentals> getAllRentals() {
        List<Rentals> rentalsList= rentalsService.getAllRentals();
        return rentalsList;
    }
}
