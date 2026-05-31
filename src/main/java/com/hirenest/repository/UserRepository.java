package com.hirenest.repository;

import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
    extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find all users by role
    // Used by admin to get all seekers/employers
    List<User> findByRole(User.Role role);

    // Count users by role
    // Used by admin for platform statistics
    long countByRole(User.Role role);
}