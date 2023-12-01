package com.rentaloc.controllers;

import com.rentaloc.models.Messages;
import com.rentaloc.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/messages")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @PostMapping("/")
    public Messages addMessages(Messages messages) {
        return messagesService.addMessages(messages);
    }


}
