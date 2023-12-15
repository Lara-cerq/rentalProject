package com.rentaloc.controllers;

import com.rentaloc.models.Messages;
import com.rentaloc.models.MessagesDto;
import com.rentaloc.models.Response;
import com.rentaloc.services.MessagesService;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RentalsService rentalsService;

    @RequestMapping(value="/api/messages/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> addMessages(@RequestBody MessagesDto messages) {
        try {

            Messages messageNew= new Messages();

            messageNew.setMessage(messages.getMessage());
            messageNew.setUsers(usersService.findUsersById(messages.getUser_id()));
            messageNew.setRentals(rentalsService.getById(messages.getRental_id()).get());

            messagesService.addMessages(messageNew);

//            Response response = new Response();
//            response.setMessage("Message send with success");
            return ResponseEntity.ok()
                    .body(new Response("Message send with success"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception ex) {
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
