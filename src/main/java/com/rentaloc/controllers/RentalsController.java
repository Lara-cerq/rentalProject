package com.rentaloc.controllers;

import com.cloudinary.Cloudinary;
import com.rentaloc.models.*;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
            // RentalsResponse = DTO qui retourne une liste de RentalToDisplay (=rentalsDisplayList)
            // RentalToDisplay = DTO qui retourne le rental au format souhaité pour l'affichage
            RentalsResponse rentalsResponse = new RentalsResponse();
            List<RentalToDisplay> rentalsDisplayList = new ArrayList<>();
            // rentalsList= liste qui retourne tous le rentals de la DB
            List<Rentals> rentalsList = rentalsService.getAllRentals();
            // boucle for qui permet de créer un RentalToDisplay
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
            // récupération du rental par l'id envoyé dans la requete HTTP
            Rentals rentals= rentalsService.getById(id);

            // création du RentalToDisplay = DTO qui retourne le rental au format souhaité pour l'afficahge
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
            // permet d'avoir les informations du user connécté à partir du token
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users userLogged= usersService.findUserByEmail(auth.getName());

            // récupération de la variable d'environnement de coudinary
            String envCloudinary = System.getenv("CLOUDINARY_URL");
            Cloudinary cloudinary = new Cloudinary(envCloudinary);

            // sauvegarde de l'image dans cloudinary avec l'uploader()
            String urlImage = cloudinary.uploader()
                    .upload(rental.getPicture().getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();

            // création du rentals
            Rentals rentalNew= new Rentals();
            rentalNew.setUsers(userLogged);
            rentalNew.setSurface(rental.getSurface());
            rentalNew.setPrice(rental.getPrice());
            rentalNew.setDescription(rental.getDescription());
            rentalNew.setName(rental.getName());
            rentalNew.setPicture(urlImage);
            // ajout du rentals dans la DB
            rentalsService.addRental(rentalNew);

            // Response = DTO qui retourne le message de succes de création
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
            // récupération du rentals à partir de son id
            Rentals updateRental = rentalsService.getById(id);
            // update du rentals
            updateRental.setName(rentalDetails.getName());
            updateRental.setDescription(rentalDetails.getDescription());
            updateRental.setPrice(rentalDetails.getPrice());
            updateRental.setSurface(rentalDetails.getSurface());
            // update dans la DB
            rentalsService.updateRental(updateRental);
            // Response retourne le message de succès
            Response response= new Response("Rental updated!");
            return ResponseEntity.ok(response.toString());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
