package com.hirenest.repository;

import com.hirenest.model.Application;
import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository
    extends JpaRepository<Application, Long> {

    // Find all applications by seeker
    // ordered by latest first
    List<Application> findBySeekerOrderByAppliedAtDesc(
        User seeker);

    // Find all applications for a job
    // ordered by latest first
    List<Application> findByJobOrderByAppliedAtDesc(
        JobListing job);

    // Check if seeker already applied
    // for a specific job
    boolean existsBySeekerAndJob(
        User seeker, JobListing job);

    // Find specific application by
    // seeker and job
    Optional<Application> findBySeekerAndJob(
        User seeker, JobListing job);

    // Count applications by seeker
    long countBySeeker(User seeker);

    // Count applications by status for seeker
    long countBySeekerAndStatus(
        User seeker,
        Application.Status status);

    // Count total applicants for employer jobs
    @Query("SELECT COUNT(a) FROM Application a " +
           "WHERE a.job.employer = :employer")
    long countByEmployer(
        @Param("employer") User employer);

    // Count by status for employer
    @Query("SELECT COUNT(a) FROM Application a " +
           "WHERE a.job.employer = :employer " +
           "AND a.status = :status")
    long countByEmployerAndStatus(
        @Param("employer") User employer,
        @Param("status") Application.Status status);

    // Find all applications for all jobs
    // posted by an employer
    @Query("SELECT a FROM Application a " +
           "WHERE a.job.employer = :employer " +
           "ORDER BY a.appliedAt DESC")
    List<Application> findByEmployer(
        @Param("employer") User employer);
}
