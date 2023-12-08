package com.rentaloc.controllers;

import com.rentaloc.models.LoginRequest;
import com.rentaloc.models.LoginResponse;
import com.rentaloc.models.Users;
import com.rentaloc.services.JWTService;
import com.rentaloc.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.crypto.EncryptedPrivateKeyInfo;


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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest user) {

        System.out.println(user.getLogin());
        System.out.println(user.getPassword());
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));

            User autendicatedUser = (User) authenticate.getPrincipal();

            String token = jwtService.generateToken(authenticate);
            // login response permet de donner la réponse dans le body avec le format que l'on veut avec "token" : "le code du token"
            LoginResponse response = new LoginResponse();
            response.setToken(token);

            return ResponseEntity.ok()
                    .body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//    @GetMapping("api/auth/me")
//    ResponseEntity<Users> getUser(Authentication authentication) {
//        try {
//
//            User autendicatedUser = (User) authentication.getPrincipal();
//
//            String email= autendicatedUser.getUsername();
//
//            Users userLogged= usersService.findUserByEmail(email);
//
//            return ResponseEntity.ok()
//                    .body(userLogged);
//
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }

    @PostMapping("api/auth/register")
    ResponseEntity<Users> addNeuwUser(@RequestBody Users userRental) {
        try {
//            String password = userRental.getPassword();
//            String encriptedPassword=
            usersService.addNewUser(userRental);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
