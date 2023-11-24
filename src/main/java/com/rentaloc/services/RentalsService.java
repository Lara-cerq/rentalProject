package com.rentaloc.services;

import com.rentaloc.models.Messages;
import com.rentaloc.models.Rentals;
import com.rentaloc.repositories.RentalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalsService {

    @Autowired
    RentalsRepository rentalsRepository;

    public List<Rentals> getAllRentals() {
        return rentalsRepository.findAll();
    }

}
