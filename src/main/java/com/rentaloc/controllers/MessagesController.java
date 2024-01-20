package com.rentaloc.controllers;

import com.rentaloc.models.Messages;
import com.rentaloc.models.MessagesRequest;
import com.rentaloc.models.Response;
import com.rentaloc.services.MessagesService;
import com.rentaloc.services.RentalsService;
import com.rentaloc.services.UsersService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@Tag(name = "Messages", description = "Contains the operation that allow to add a new message")
@SecurityRequirement(name = "Bearer Authentication")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private RentalsService rentalsService;

    @RequestMapping(value="api/messages", method = RequestMethod.POST, produces =  { "application/json" })
    public ResponseEntity<Response> addMessages(@RequestBody MessagesRequest messages) {
        try {

            Messages messageNew= new Messages();

            messageNew.setMessage(messages.getMessage());
            messageNew.setUsers(usersService.findUsersById(messages.getUser_id()));
            messageNew.setRentals(rentalsService.getById(messages.getRental_id()));

            messagesService.addMessages(messageNew);

           Response response = new Response("Message send with success");
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
