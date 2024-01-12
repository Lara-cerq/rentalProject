package com.rentaloc.controllers;

import com.rentaloc.models.*;
import com.rentaloc.services.JWTService;
import com.rentaloc.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;


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

    @RequestMapping(value="api/auth/me", method = RequestMethod.GET, produces =  { "application/json" })
    public UserResponse getUser( ) {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            Users userLogged= usersService.findUserByEmail(auth.getName());

            UserResponse user= new UserResponse();
            user.setId(userLogged.getId());
            user.setName(userLogged.getName());
            user.setEmail(userLogged.getEmail());
            user.setCreated_at(userLogged.getCreated_at());
            user.setUpdated_at(userLogged.getUpdated_at());
            return user;

        } catch (BadCredentialsException ex) {
            return null;
        }
    }

    @RequestMapping(value = "api/auth/register", method = RequestMethod.POST, produces =  { "application/json" } )
    public LoginResponse addNeuwUser(@RequestBody RegisterRequest userRental) {
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

                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
