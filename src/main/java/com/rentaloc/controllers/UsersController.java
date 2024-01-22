package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@Tag(name = "Users", description = "Contains an operation that allows to informations of user.")
@SecurityRequirement(name = "Bearer Authentication")
public class UsersController {

    @Autowired
    UsersService usersService;

    @GetMapping(value = "api/user/{id}", produces =  { "application/json" } )
    public UserResponse getUserById(@PathVariable("id") Integer id) {

        try {
            // récupération de l'utilisateur par l'id
            Users user= usersService.findUsersById(id);

            // UserResponse = DTO retourne les informations du user requises sans le password par exemple
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setName(user.getName());
            userResponse.setEmail(user.getEmail());
            userResponse.setCreated_at(user.getCreated_at());
            userResponse.setUpdated_at(user.getUpdated_at());

            return userResponse;
        } catch (BadCredentialsException ex) {
            return null;
        }
    }




}
