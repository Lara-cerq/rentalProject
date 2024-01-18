package com.rentaloc.repositories;

import com.rentaloc.models.Rentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalsRepository extends JpaRepository<Rentals, Integer> {

    public Rentals findRentalById(Integer id);
}
