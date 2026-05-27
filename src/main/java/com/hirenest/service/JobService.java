package com.hirenest.service;

import com.hirenest.dto.JobListingRequest;
import com.hirenest.model.JobListing;
import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.JobListingRepository;
import com.hirenest.repository.SeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private SeekerProfileRepository
        seekerProfileRepository;

    // ─── EMPLOYER METHODS ─────────────────────

    // Post new job
    public void postJob(User employer,
        JobListingRequest request) {

        JobListing job = new JobListing();
        job.setEmployer(employer);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setRequiredSkills(
            request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setSalaryRange(request.getSalaryRange());
        job.setJobType(request.getJobType());
        job.setDeadline(request.getDeadline());
        job.setActive(true);

        jobListingRepository.save(job);
    }

    // Get jobs posted by employer
    public List<JobListing> getEmployerJobs(
        User employer) {
        return jobListingRepository
            .findByEmployerOrderByPostedAtDesc(
                employer);
    }

    // Get job by ID
    public Optional<JobListing> getJobById(
        Long jobId) {
        return jobListingRepository.findById(jobId);
    }

    // Close job listing
    public void closeJob(Long jobId) {
        jobListingRepository.findById(jobId)
            .ifPresent(job -> {
                job.setActive(false);
                jobListingRepository.save(job);
            });
    }

    // Reopen job listing
    public void reopenJob(Long jobId) {
        jobListingRepository.findById(jobId)
            .ifPresent(job -> {
                job.setActive(true);
                jobListingRepository.save(job);
            });
    }

    // ─── SEEKER METHODS ───────────────────────

    // Get all active jobs
    public List<JobListing> getAllActiveJobs() {
        return jobListingRepository
            .findByIsActiveTrueOrderByPostedAtDesc();
    }

    // Search jobs with filters
    public List<JobListing> searchJobs(
        String keyword,
        String location,
        String jobType) {

        JobListing.JobType type = null;
        if (jobType != null
            && !jobType.isEmpty()) {
            try {
                type = JobListing.JobType
                    .valueOf(jobType);
            } catch (Exception e) {
                type = null;
            }
        }

        // If no filters apply get all jobs
        if ((keyword == null
            || keyword.isEmpty())
            && (location == null
            || location.isEmpty())
            && type == null) {
            return getAllActiveJobs();
        }

        return jobListingRepository
            .searchWithFilters(
                keyword, location, type);
    }

    // ─── RECOMMENDATION ENGINE ────────────────

    public List<JobListing> getRecommendedJobs(
        User seeker) {

        // Get seeker skills from profile
        Optional<SeekerProfile> profileOpt =
            seekerProfileRepository
                .findByUser(seeker);

        // If no profile or no skills
        // return all active jobs
        if (profileOpt.isEmpty()) {
            return getAllActiveJobs();
        }

        SeekerProfile profile = profileOpt.get();
        String skills = profile.getSkills();

        if (skills == null || skills.isEmpty()) {
            return getAllActiveJobs();
        }

        // Parse seeker skills into list
        List<String> seekerSkills = Arrays
            .stream(skills.split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());

        // Get all active jobs
        List<JobListing> allJobs =
            getAllActiveJobs();

        // Score each job by skill match
        List<JobListing> recommendedJobs =
            new ArrayList<>();

        for (JobListing job : allJobs) {
            if (job.getRequiredSkills() != null
                && !job.getRequiredSkills()
                .isEmpty()) {

                List<String> jobSkills = Arrays
                    .stream(job.getRequiredSkills()
                        .split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

                // Check if any skill matches
                boolean hasMatch = seekerSkills
                    .stream()
                    .anyMatch(seekerSkill ->
                        jobSkills.stream()
                            .anyMatch(jobSkill ->
                                jobSkill.contains(
                                    seekerSkill) ||
                                seekerSkill.contains(
                                    jobSkill)));

                if (hasMatch) {
                    recommendedJobs.add(job);
                }
            }
        }

        // If no matches return all jobs
        if (recommendedJobs.isEmpty()) {
            return allJobs;
        }

        return recommendedJobs;
    }

    // Calculate match percentage
    public int calculateMatchPercentage(
        JobListing job, User seeker) {

        Optional<SeekerProfile> profileOpt =
            seekerProfileRepository
                .findByUser(seeker);

        if (profileOpt.isEmpty()) return 0;

        String skills = profileOpt.get()
            .getSkills();
        if (skills == null
            || skills.isEmpty()) return 0;

        List<String> seekerSkills = Arrays
            .stream(skills.split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());

        if (job.getRequiredSkills() == null
            || job.getRequiredSkills().isEmpty())
            return 0;

        List<String> jobSkills = Arrays
            .stream(job.getRequiredSkills()
                .split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());

        long matchCount = seekerSkills.stream()
            .filter(seekerSkill ->
                jobSkills.stream()
                    .anyMatch(jobSkill ->
                        jobSkill.contains(
                            seekerSkill) ||
                        seekerSkill.contains(
                            jobSkill)))
            .count();

        return (int) ((matchCount * 100)
            / jobSkills.size());
    }
}