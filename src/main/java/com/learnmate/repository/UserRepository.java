package com.learnmate.repository;

import com.learnmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Make sure this import is present
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This method allows login with either username or email
    Optional<User> findByUsernameOrEmail(String username, String email);

    // THIS IS THE NEW METHOD THAT WAS MISSING
    // It will automatically generate a query to find all users with a specific role.
    List<User> findByRole(String role);

}