package com.hirenest.repository;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployerProfileRepository
    extends JpaRepository<EmployerProfile, Long> {

    // Find profile by user
    Optional<EmployerProfile> findByUser(User user);

    // Find all pending approval profiles
    // Used by admin
    List<EmployerProfile> findByIsApprovedFalse();

    // Find all approved profiles
    List<EmployerProfile> findByIsApprovedTrue();

    // Count pending employers
    // Used by admin dashboard stats
    long countByIsApprovedFalse();
}