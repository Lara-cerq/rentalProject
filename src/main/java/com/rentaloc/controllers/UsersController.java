package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class UsersController {

    @Autowired
    UsersService usersService;

    @GetMapping(value = "api/user/{id}", produces =  { "application/json" } )
    public UserResponse getUserById(@PathVariable("id") Integer id) {

        try {
            Users user= usersService.findUsersById(id);

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
