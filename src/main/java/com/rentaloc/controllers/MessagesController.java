package com.rentaloc.controllers;

import com.rentaloc.models.Messages;
import com.rentaloc.models.MessagesDto;
import com.rentaloc.models.Response;
import com.rentaloc.services.MessagesService;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RentalsService rentalsService;

    @PostMapping("api/messages/")
    public ResponseEntity<Response> addMessages(@RequestBody Messages messages) {
        try {

            Messages messageNew= new Messages();

            messageNew.setMessage(messages.getMessage());
            messageNew.setUsers(usersService.findUsersById(messages.getUsers().getId()));
            messageNew.setRentals(rentalsService.getById(messages.getRentals().getId()).get());

            messagesService.addMessages(messageNew);

            Response response = new Response();
            response.setMessage("message");
            response.setText("Message send with success");
            return ResponseEntity.ok()
                    .body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
