package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.JWTService;
import com.rentaloc.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class LoginController {

    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService usersService;

    public LoginController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    //Login Request permet de renvoyer le login et password dans le body du post
    @PostMapping("api/auth/login")
    public LoginResponse login(@RequestBody LoginRequest user) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            User autendicatedUser = (User) authenticate.getPrincipal();

            String token = jwtService.generateToken(authenticate);
            // login response permet de donner la réponse dans le body avec le format que l'on veut avec "token" : "le code du token"
            LoginResponse response = new LoginResponse();
            response.setToken(token);

            return response;

        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @GetMapping("api/auth/me")
    public Users getUser( ) {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Users userLogged= usersService.findUserByEmail(auth.getName());

            //Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            //for (SimpleGrantedAuthority authority : authorities)
           // {
               // roles.add(authority.getAuthority());
            //}
            //userReturnData.setRoles(roles);

//            User autendicatedUser = (User) authentication.getCredentials();
//
//            String email= autendicatedUser.getUsername();
//
//            Users userLogged= usersService.findUserByEmail(email);

            return userLogged;

        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @PostMapping("api/auth/register")
    public LoginResponse addNeuwUser(@RequestBody RegisterRequest userRental) {
        try {
//            String password = userRental.getPassword();
//            String encriptedPassword=
            Users user= new Users();
            user.setEmail(userRental.getEmail());
            user.setName(userRental.getName());
            user.setPassword(userRental.getPassword());
            usersService.addNewUser(user);

            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            User autendicatedUser = (User) authenticate.getPrincipal();

            String token = jwtService.generateToken(authenticate);
            // login response permet de donner la réponse dans le body avec le format que l'on veut avec "token" : "le code du token"
            LoginResponse response = new LoginResponse();
            response.setToken(token);

            return response;
        } catch (Exception e) {
            return null;
        }
    }

}
