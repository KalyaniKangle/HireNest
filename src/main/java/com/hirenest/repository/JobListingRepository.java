package com.hirenest.repository;

import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobListingRepository
    extends JpaRepository<JobListing, Long> {

    // Find all active jobs
    List<JobListing> findByIsActiveTrueOrderByPostedAtDesc();

    // Find jobs by employer
    List<JobListing> findByEmployerOrderByPostedAtDesc(
        User employer);

    // Search jobs by keyword
    @Query("SELECT j FROM JobListing j WHERE " +
        "j.isActive = true AND (" +
        "LOWER(j.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
        "LOWER(j.description) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
        "LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    List<JobListing> searchByKeyword(
        @Param("keyword") String keyword);

    // Filter by location
    @Query("SELECT j FROM JobListing j WHERE " +
        "j.isActive = true AND " +
        "LOWER(j.location) LIKE LOWER(CONCAT('%',:location,'%'))")
    List<JobListing> findByLocation(
        @Param("location") String location);

    // Filter by job type
    List<JobListing> findByIsActiveTrueAndJobType(
        JobListing.JobType jobType);

    // Search with multiple filters
    @Query("SELECT j FROM JobListing j WHERE " +
        "j.isActive = true AND " +
        "(:keyword IS NULL OR " +
        "LOWER(j.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
        "LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%',:keyword,'%'))) AND " +
        "(:location IS NULL OR " +
        "LOWER(j.location) LIKE LOWER(CONCAT('%',:location,'%'))) AND " +
        "(:jobType IS NULL OR " +
        "j.jobType = :jobType)")
    List<JobListing> searchWithFilters(
        @Param("keyword") String keyword,
        @Param("location") String location,
        @Param("jobType") JobListing.JobType jobType);
}