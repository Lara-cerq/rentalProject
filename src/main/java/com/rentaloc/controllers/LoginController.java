package com.rentaloc.controllers;

import com.rentaloc.services.JWTService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private JWTService jwtService;

    public LoginController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("api/auth/login")
    public String getToken(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        return token;
    }
}
