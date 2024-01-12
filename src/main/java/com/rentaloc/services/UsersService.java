package com.rentaloc.services;

import com.rentaloc.models.Users;
import com.rentaloc.repositories.UsersRepository;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    UsersRepository usersRepository;

    public Users addNewUser(Users user) {
        return usersRepository.save(user);
    }

    public Users findUserByEmail(String email) {
        return usersRepository.findUserByEmail(email);
    }
    public Users findUserByName(String name) {
        return usersRepository.findUserByName(name);
    }

    public Users findUsersById(Integer id) {
        return usersRepository.findUserById(id);
    }

    public boolean ifEmailExists(String email) {
        boolean userInDb= true;
        if(usersRepository.findUserByEmail(email) == null) {
            userInDb=false;
        }
        return userInDb;
    }

    public boolean ifUsernameExists(String username) {
        boolean userInDb= true;
        if(usersRepository.findUserByName(username) == null) {
            userInDb=false;
        }
        return userInDb;
    }
}
