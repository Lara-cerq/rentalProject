package com.rentaloc.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RessourceController {

    @GetMapping("/")
    // il faut utiliser barer token et mettre le token qui est généré avec authentification
    public String getResource() {
        return "a value...";
    }
}
