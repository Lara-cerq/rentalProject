package com.rentaloc.controllers;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import com.rentaloc.models.Messages;
import com.rentaloc.models.Rentals;
import com.rentaloc.models.Response;
import com.rentaloc.services.RentalsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class RentalsController {

    @Autowired
    RentalsService rentalsService;

    @GetMapping("api/rentals")
    public ResponseEntity<Iterable<Rentals>>getAllRentals() {
        try {
            Iterable<Rentals> rentalsList = rentalsService.getAllRentals();

            return ResponseEntity.ok()
                    .body(rentalsList);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("api/rentals/{id}")
    public ResponseEntity<Rentals> getRentalById(@PathVariable("id") Integer id) {

        try {
            Rentals rentals= rentalsService.getById(id).get();;

            return ResponseEntity.ok()
                    .body(rentals);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "api/rentals/{id}", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Response> saveRentals(@PathVariable("id") Integer id, @RequestBody Rentals rental) {
        try {
            rental.setId(id);
            rentalsService.addRental(rental);
            Response response = new Response();
            response.setMessage("message");
            response.setText("Rental created!");
            return ResponseEntity.ok()
                    .body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("api/rentals/{id}")
    public ResponseEntity<Response> updateRentals(@PathVariable(required = true, name = "id") Integer id, @RequestBody Rentals rentalDetails) {

        try {

            Rentals updateRental = rentalsService.getById(id).get();
            updateRental.setName(rentalDetails.getName());
            updateRental.setDescription(rentalDetails.getDescription());
            updateRental.setPicture(rentalDetails.getPicture());
            updateRental.setPrice(rentalDetails.getPrice());
            updateRental.setSurface(rentalDetails.getSurface());

            rentalsService.updateRental(updateRental);

            Response response = new Response();
            response.setMessage("message");
            response.setText("Rental updated!");
            return ResponseEntity.ok()
                    .body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
