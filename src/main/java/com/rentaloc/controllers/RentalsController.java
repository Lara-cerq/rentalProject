package com.rentaloc.controllers;

import com.cloudinary.Cloudinary;
import com.rentaloc.models.*;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@Tag(name = "Rentals", description = "Contains the operations that allows add or update a rental and get informations")
@SecurityRequirement(name = "Bearer Authentication")
public class RentalsController {

    @Autowired
    RentalsService rentalsService;

    @Autowired
    UsersService usersService;

    @GetMapping("api/rentals")
    public ResponseEntity<RentalsResponse> getAllRentals() {
        try {
            RentalsResponse rentalsResponse = new RentalsResponse();
            List<RentalToDisplay> rentalsDisplayList = new ArrayList<>();
            List<Rentals> rentalsList = rentalsService.getAllRentals();
            for (Rentals rental : rentalsList) {
                RentalToDisplay rentalToDisplay = new RentalToDisplay(rental.getId(),rental.getName(), rental.getSurface(),
                rental.getPrice(),rental.getPicture(),rental.getDescription(),rental.getUsers().getId(),rental.getUsers().getCreated_at(),
                rental.getUsers().getUpdated_at());
                rentalsDisplayList.add(rentalToDisplay);
                }
            rentalsResponse.setRentals(rentalsDisplayList);

        return ResponseEntity.ok(rentalsResponse);
    } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value = "api/rentals/{id}", produces =  { "application/json" })
    public ResponseEntity<RentalToDisplay> getRentalById(@PathVariable("id") Integer id) {

        try {
            Rentals rentals= rentalsService.getById(id);

            RentalToDisplay rentalToDisplay= new RentalToDisplay();
            rentalToDisplay.setId(rentals.getId());
            rentalToDisplay.setName(rentals.getName());
            rentalToDisplay.setPrice(rentals.getPrice());
            rentalToDisplay.setSurface(rentals.getSurface());
            rentalToDisplay.setPicture(rentals.getPicture());
            rentalToDisplay.setDescription(rentals.getDescription());
            rentalToDisplay.setOwner_id(rentals.getUsers().getId());
            rentalToDisplay.setCreated_at(rentals.getUsers().getCreated_at());
            rentalToDisplay.setUpdated_at(rentals.getUsers().getUpdated_at());
            return ResponseEntity.ok(rentalToDisplay);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "api/rentals", method = RequestMethod.POST, produces =  { "application/json" })
    public ResponseEntity<String> saveRentals(@ModelAttribute RentalsFormData rental){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users userLogged= usersService.findUserByEmail(auth.getName());

            Path publicDirectory = Paths.get( "picture").toAbsolutePath();
            byte[] imageContent = rental.getPicture().getBytes();
            Path filepath = Paths.get(publicDirectory.toString(), rental.getPicture().getOriginalFilename());


            Cloudinary cloudinary = new Cloudinary("cloudinary://344757681628711:wmAlqMGlVc8Dx-u4tUvh4ySNnlo@dthhif8wt");

            String urlImage = cloudinary.uploader() // Upload the file to Cloudinary using the Cloudinary uploader
                    // The uploader().upload() method takes the file's bytes and the public ID as
                    // parameters
                    .upload(rental.getPicture().getBytes(),
                            // It uploads the file to the Cloudinary service with the specified public ID.
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url") // Get the URL of the uploaded file from the response
                    .toString();


            Rentals rentalNew= new Rentals();
            rentalNew.setUsers(userLogged);
            rentalNew.setSurface(rental.getSurface());
            rentalNew.setPrice(rental.getPrice());
            rentalNew.setDescription(rental.getDescription());
            rentalNew.setName(rental.getName());
            rentalNew.setPicture(urlImage); // a remplacer par le path de https
            System.out.println(filepath);

            rentalsService.addRental(rentalNew);

            Response response = new Response("Rental created!");
            return ResponseEntity.ok(response.toString());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="api/rentals/{id}", method= RequestMethod.PUT, produces =  { "application/json" })
    public ResponseEntity<String> updateRentals(@PathVariable(required = true, name = "id") Integer id, @ModelAttribute RentalsForUpdateFormData rentalDetails) {

        try {

            Rentals updateRental = rentalsService.getById(id);
            updateRental.setName(rentalDetails.getName());
            updateRental.setDescription(rentalDetails.getDescription());
            updateRental.setPrice(rentalDetails.getPrice());
            updateRental.setSurface(rentalDetails.getSurface());

            rentalsService.updateRental(updateRental);
            Response response= new Response("Rental updated!");
            return ResponseEntity.ok(response.toString());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
