package com.scm.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.Users;

@Repository
public interface UserRepo extends JpaRepository<Users,String> {
    Users findByEmail(String email);

    Optional<Users> findByEmailAndPassword(String email, String password);

    // Optional<Users> findByEmailToken(String id);
}
