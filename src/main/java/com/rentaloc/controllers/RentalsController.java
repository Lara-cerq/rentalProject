package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
    public RentalsResponse getAllRentals() {
        try {
            RentalToDisplay rentalToDisplay= new RentalToDisplay();
            List<RentalToDisplay> rentalsDisplayList= new ArrayList<>();
            List<Rentals> rentalsList = rentalsService.getAllRentals();
            for (Rentals rental : rentalsList) {
                rentalToDisplay.setId(rental.getId());
                rentalToDisplay.setName(rental.getName());
                rentalToDisplay.setSurface(rental.getSurface());
                rentalToDisplay.setPrice(rental.getPrice());
                rentalToDisplay.setPicture(rental.getPicture());
                rentalToDisplay.setDescription(rental.getDescription());
                rentalToDisplay.setOwner_id(rental.getUsers().getId());
                rentalToDisplay.setCreated_at(rental.getUsers().getCreated_at());
                rentalToDisplay.setUpdated_at(rental.getUsers().getUpdated_at());
                rentalsDisplayList.add(rentalToDisplay);
            }
            RentalsResponse rentalsResponse = new RentalsResponse();
            rentalsResponse.setRentals(rentalsDisplayList);
            return rentalsResponse;
        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @GetMapping(value = "api/rentals/{id}", produces =  { "application/json" })
    public RentalToDisplay getRentalById(@PathVariable("id") Integer id) {

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
            return rentalToDisplay;
        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @RequestMapping(value = "api/rentals", method = RequestMethod.POST, produces =  { "application/json" })
    public String saveRentals(@ModelAttribute RentalsFormData rental){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users userLogged= usersService.findUserByEmail(auth.getName());

            Path publicDirectory = Paths.get( "picture").toAbsolutePath();
            byte[] imageContent = rental.getPicture().getBytes();
            Path filepath = Paths.get(publicDirectory.toString(), rental.getPicture().getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(filepath)) {
                os.write(imageContent);
            }

            Rentals rentalNew= new Rentals();
            rentalNew.setUsers(userLogged);
            rentalNew.setSurface(rental.getSurface());
            rentalNew.setPrice(rental.getPrice());
            rentalNew.setDescription(rental.getDescription());
            rentalNew.setName(rental.getName());
            rentalNew.setPicture(filepath.toString());
            System.out.println(filepath);

            rentalsService.addRental(rentalNew);

            Response response = new Response("Rental created!");
            return response.toString();
        } catch (BadCredentialsException ex) {
            return ("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(
            value = "/get-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
//    public @ResponseBody byte[] getImage(@RequestParam Integer id) throws IOException {
//        Path imagePath = getImagePathFromDb(id);
//        return Files.readAllBytes(imagePath);
//    }

    @RequestMapping(value="api/rentals/{id}", method= RequestMethod.PUT, produces =  { "application/json" })
    public String updateRentals(@PathVariable(required = true, name = "id") Integer id, @ModelAttribute RentalsForUpdateFormData rentalDetails) {

        try {

            Rentals updateRental = rentalsService.getById(id);
            updateRental.setName(rentalDetails.getName());
            updateRental.setDescription(rentalDetails.getDescription());
            updateRental.setPrice(rentalDetails.getPrice());
            updateRental.setSurface(rentalDetails.getSurface());

            rentalsService.updateRental(updateRental);
            Response response= new Response("Rental updated!");
            return response.toString();
        } catch (BadCredentialsException ex) {
            return "";
        }
    }
}
