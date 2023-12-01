package com.rentaloc.services;

import com.rentaloc.models.Messages;
import com.rentaloc.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagesService {

    @Autowired
    private MessagesRepository messagesRepository;


    public Messages addMessages(Messages message) {
        return messagesRepository.save(message);
    }

}
