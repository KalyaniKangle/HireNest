package com.hirenest.service;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import com.hirenest.repository.ApplicationRepository;
import com.hirenest.repository.EmployerProfileRepository;
import com.hirenest.repository.JobListingRepository;
import com.hirenest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerProfileRepository
        employerProfileRepository;

    @Autowired
    private JobListingRepository
        jobListingRepository;

    @Autowired
    private ApplicationRepository
        applicationRepository;

    // ─── PLATFORM STATISTICS ──────────────────

    public long getTotalSeekers() {
        return userRepository
            .countByRole(User.Role.SEEKER);
    }

    public long getTotalEmployers() {
        return userRepository
            .countByRole(User.Role.EMPLOYER);
    }

    public long getTotalJobs() {
        return jobListingRepository.count();
    }

    public long getTotalApplications() {
        return applicationRepository.count();
    }

    public long getPendingEmployersCount() {
        return employerProfileRepository
            .countByIsApprovedFalse();
    }

    public long getActiveJobs() {
        return jobListingRepository
            .countByIsActiveTrue();
    }

    // ─── USER MANAGEMENT ──────────────────────

    // Get all seekers
    public List<User> getAllSeekers() {
        return userRepository
            .findByRole(User.Role.SEEKER);
    }

    // Get all employers
    public List<User> getAllEmployers() {
        return userRepository
            .findByRole(User.Role.EMPLOYER);
    }

    // Block a user
    public void blockUser(Long userId) {
        userRepository.findById(userId)
            .ifPresent(user -> {
                user.setActive(false);
                userRepository.save(user);
            });
    }

    // Unblock a user
    public void unblockUser(Long userId) {
        userRepository.findById(userId)
            .ifPresent(user -> {
                user.setActive(true);
                userRepository.save(user);
            });
    }

    // ─── EMPLOYER APPROVAL ────────────────────

    // Get all employer profiles
    public List<EmployerProfile>
        getAllEmployerProfiles() {
        return employerProfileRepository.findAll();
    }

    // Get pending employer profiles
    public List<EmployerProfile>
        getPendingEmployers() {
        return employerProfileRepository
            .findByIsApprovedFalse();
    }

    // Approve employer
    public void approveEmployer(Long profileId) {
        employerProfileRepository
            .findById(profileId)
            .ifPresent(profile -> {
                profile.setApproved(true);
                employerProfileRepository
                    .save(profile);
            });
    }

    // Reject/Unapprove employer
    public void rejectEmployer(Long profileId) {
        employerProfileRepository
            .findById(profileId)
            .ifPresent(profile -> {
                profile.setApproved(false);
                employerProfileRepository
                    .save(profile);
            });
    }

    // ─── JOB MANAGEMENT ───────────────────────

    // Get all job listings
    public List<JobListing> getAllJobs() {
        return jobListingRepository
            .findAll();
    }

    // Remove a job listing
    public void removeJob(Long jobId) {
        jobListingRepository.deleteById(jobId);
    }

    // Get employer profile by user
    public Optional<EmployerProfile>
        getEmployerProfile(User employer) {
        return employerProfileRepository
            .findByUser(employer);
    }
}
