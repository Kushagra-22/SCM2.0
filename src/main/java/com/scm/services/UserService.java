package com.scm.services;

import java.util.List;
import java.util.Optional;

import com.scm.entities.Users;

@org.springframework.stereotype.Service
public interface UserService {
    Users saveUser(Users user);

    Optional<Users> getUserById(String id);

    Optional<Users>  updateUser(Users user);

    void deleteUser(String id);

    List<Users>getAllUsers();

    Users getUserByEmail(String email);

    boolean isUserExistByEmail(String email);



}
