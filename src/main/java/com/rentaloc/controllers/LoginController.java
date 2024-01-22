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

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersService usersService;

    //Login Request= DTO qui permet d'envoyer le mail + passoword
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest user) {
        try {
            // verifie que le mail et le password existent dans la base de données
            // si existe => on génére le token / sinon erreur 401 = unauthorized
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            String token = jwtService.generateToken(authenticate);
            // login response = DTO qui permet d'afficher le token au format demandé
            LoginResponse response = new LoginResponse();
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("error"));
        }
    }

    @RequestMapping(value="/me", method = RequestMethod.GET, produces =  { "application/json" })
    public ResponseEntity<UserResponse> getUser( ) {
        try {
            // permet d'avoir les données de la personne qui est connecté à partir du token
            // si pas de token => erreur 401 unauthorized
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // récupére la personne qui est connectée
            Users userLogged= usersService.findUserByEmail(auth.getName());

            // UserResponse= DTO qui permet de créer un user au format que l'on veut l'afficher
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
           // permet de vérifier si n'xiste pas dans la DB
           // si n'existe pas => on va l'ajouter
           // si existe deja => Exception : NON ACCEPTABLE
           // si on ne peut pas ajouter un user alors BAD REQUEST
            if ( usersService.findUserByEmail(userRental.getEmail()) ==null ) {

                String password = userRental.getPassword();
                String encryptedPassword;

                // encryptage du password
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                encryptedPassword = encoder.encode(password);

                // nouveau user crée
                Users user = new Users();
                user.setEmail(userRental.getEmail());
                user.setName(userRental.getName());
                user.setPassword(encryptedPassword);
                // ajout de l'utilsateur
                usersService.addNewUser(user);

                //  permet d'avoir les informations du nouveau utilisateur afin de pouvoir envoyer le token
                Authentication authenticate = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(userRental.getEmail(), userRental.getPassword()));

                String token = jwtService.generateToken(authenticate);

                // LoginResponse = DTO qui permet d'envoyer le token
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
