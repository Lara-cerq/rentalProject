package com.rentaloc.repositories;

import com.rentaloc.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    public Users findUserByEmail (String email);
}
