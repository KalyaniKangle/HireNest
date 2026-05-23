package com.hirenest.repository;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployerProfileRepository
    extends JpaRepository<EmployerProfile, Long> {

    Optional<EmployerProfile> findByUser(User user);

    boolean existsByUser(User user);
}