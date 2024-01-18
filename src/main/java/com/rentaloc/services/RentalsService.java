package com.rentaloc.services;

import com.rentaloc.models.Messages;
import com.rentaloc.models.Rentals;
import com.rentaloc.repositories.RentalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalsService {

    @Autowired
    RentalsRepository rentalsRepository;

    public List<Rentals> getAllRentals() {
        return rentalsRepository.findAll();
    }

    public Rentals getById(Integer id) {
        return rentalsRepository.findRentalById(id);
    }

    public Rentals addRental(Rentals rental) {
        return rentalsRepository.save(rental);
    }

    public Rentals updateRental (Rentals rental) {
        return rentalsRepository.save(rental);
    }

}
