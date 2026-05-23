package com.hirenest.repository;

import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SeekerProfileRepository
    extends JpaRepository<SeekerProfile, Long> {

    Optional<SeekerProfile> findByUser(User user);

    boolean existsByUser(User user);
}