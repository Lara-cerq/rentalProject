package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.JWTService;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Contains the operations that allows to login, register an user and get informations of user logged.")
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
//    @RequestMapping(value="/login", method = RequestMethod.POST, produces =  { "application/json" })
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
//        try {
//
//            Authentication authenticate = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
//
//
//            String token = jwtService.generateToken(authenticate);
//            // login response permet de donner la réponse dans le body avec le format que l'on veut avec "token" : "le code du token"
//            LoginResponse response = new LoginResponse();
//            response.setToken(token);
//
//            return ResponseEntity.ok(response);
//
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            User autendicatedUser = (User) authenticate.getPrincipal();

            String token = jwtService.generateToken(authenticate);
            // login response permet de donner la réponse dans le body avec le format que l'on veut avec "token" : "le code du token"
            LoginResponse response = new LoginResponse();
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @RequestMapping(value="/me", method = RequestMethod.GET, produces =  { "application/json" })
    public ResponseEntity<UserResponse> getUser( ) {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Users userLogged= usersService.findUserByEmail(auth.getName());

            UserResponse user= new UserResponse();
            user.setId(userLogged.getId());
            user.setName(userLogged.getName());
            user.setEmail(userLogged.getEmail());
            user.setCreated_at(userLogged.getCreated_at());
            user.setUpdated_at(userLogged.getUpdated_at());
            return ResponseEntity.ok(user);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces =  { "application/json" } )
    public ResponseEntity<LoginResponse> addNeuwUser(@RequestBody RegisterRequest userRental) {
       try {
            if ( usersService.findUserByEmail(userRental.getEmail()) ==null ) {

                String password = userRental.getPassword();
                String encryptedPassword;

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                encryptedPassword = encoder.encode(password);

                Users user = new Users();
                user.setEmail(userRental.getEmail());
                user.setName(userRental.getName());
                user.setPassword(encryptedPassword);
                usersService.addNewUser(user);

                Authentication authenticate = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(userRental.getEmail(), userRental.getPassword()));

                User autendicatedUser = (User) authenticate.getPrincipal();

                String token = jwtService.generateToken(authenticate);

                LoginResponse response = new LoginResponse();
                response.setToken(token);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
