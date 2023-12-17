package com.rentaloc.controllers;

import ch.qos.logback.classic.util.CopyOnInheritThreadLocal;
import com.rentaloc.configuration.FileUploadUtil;
import com.rentaloc.models.*;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class RentalsController {

    @Autowired
    RentalsService rentalsService;

    @Autowired
    UsersService usersService;

    @GetMapping("api/rentals")
    public RentalsResponse getAllRentals() {
        try {
            List<Rentals> rentalsList = rentalsService.getAllRentals();

            return new RentalsResponse(rentalsList);
        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @GetMapping("api/rentals/{id}")
    public Rentals getRentalById(@PathVariable("id") Integer id) {

        try {
            Rentals rentals= rentalsService.getById(id).get();;

            return rentals;
        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @GetMapping("/rentals/create")
    public String addRentalsForm(Rentals rentals) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Users userLogged= usersService.findUserByEmail(auth.getName());
        Integer id= userLogged.getId();
        return "api/rentals/"+ id;
    }

    @RequestMapping(value = "api/rentals", method = RequestMethod.POST)
    public Response saveRentals(@ModelAttribute RentalsFormData rental){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Users userLogged= usersService.findUserByEmail(auth.getName());
            //rental.setUsers(userLogged);

//            String fileName = StringUtils.cleanPath(picture.getOriginalFilename());
//            rental.setPicture(fileName);

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

            rentalsService.addRental(rentalNew);

//            String uploadDir = "rental-photos/" + rentalSaved.getId();
//
//            FileUploadUtil.saveFile(uploadDir, fileName, picture);



            System.out.println(filepath.toString());
            Response response = new Response("Rental created!");
            return response;
        } catch (BadCredentialsException ex) {
            return new Response("");
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

    @PutMapping("api/rentals/{id}")
    public Response updateRentals(@PathVariable(required = true, name = "id") Integer id, @RequestBody Rentals rentalDetails) {

        try {

            Rentals updateRental = rentalsService.getById(id).get();
            updateRental.setName(rentalDetails.getName());
            updateRental.setDescription(rentalDetails.getDescription());
            updateRental.setPicture(rentalDetails.getPicture());
            updateRental.setPrice(rentalDetails.getPrice());
            updateRental.setSurface(rentalDetails.getSurface());

            rentalsService.updateRental(updateRental);

            return new Response("Rental updated!");
        } catch (BadCredentialsException ex) {
            return new Response("");
        }
    }
}
