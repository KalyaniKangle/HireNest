package com.hirenest.service;
 
import com.hirenest.dto.JobListingRequest;
import com.hirenest.model.JobListing;
import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.JobListingRepository;
import com.hirenest.repository.SeekerProfileRepository;
import com.hirenest.util.Skillsynonymutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Service
public class JobService {
 
    @Autowired
    private JobListingRepository
        jobListingRepository;
 
    @Autowired
    private SeekerProfileRepository
        seekerProfileRepository;
 
    // ← Module 7: AI Skill Matching Engine
    @Autowired
    private Skillsynonymutil skillSynonymUtil;
 
    // ─── EMPLOYER METHODS ─────────────────────
 
    // Post new job
    public void postJob(User employer,
        JobListingRequest request) {
 
        JobListing job = new JobListing();
        job.setEmployer(employer);
        job.setTitle(request.getTitle());
        job.setDescription(
            request.getDescription());
        job.setRequiredSkills(
            request.getRequiredSkills());
        job.setLocation(request.getLocation());
        job.setSalaryRange(
            request.getSalaryRange());
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
        return jobListingRepository
            .findById(jobId);
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
 
    // ─── AI RECOMMENDATION ENGINE ─────────────
    // Module 7: Upgraded with semantic matching
    // Uses SkillSynonymUtil for intelligent
    // skill comparison beyond simple string match
 
    public List<JobListing> getRecommendedJobs(
        User seeker) {
 
        Optional<SeekerProfile> profileOpt =
            seekerProfileRepository
                .findByUser(seeker);
 
        if (profileOpt.isEmpty()) {
            return getAllActiveJobs();
        }
 
        SeekerProfile profile =
            profileOpt.get();
        String skills = profile.getSkills();
 
        if (skills == null
            || skills.isEmpty()) {
            return getAllActiveJobs();
        }
 
        // Parse seeker skills
        List<String> seekerSkills = Arrays
            .stream(skills.split(","))
            .map(String::trim)
            .map(String::toLowerCase)
            .collect(Collectors.toList());
 
        List<JobListing> allJobs =
            getAllActiveJobs();
 
        // Score each job using AI matching
        List<JobListing> recommendedJobs =
            new ArrayList<>();
 
        for (JobListing job : allJobs) {
            if (job.getRequiredSkills() != null
                && !job.getRequiredSkills()
                    .isEmpty()) {
 
                List<String> jobSkills = Arrays
                    .stream(
                        job.getRequiredSkills()
                            .split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
 
                // Module 7: Use semantic
                // matching instead of simple
                // string contains check
                boolean hasMatch =
                    seekerSkills.stream()
                        .anyMatch(seekerSkill ->
                            jobSkills.stream()
                                .anyMatch(jobSkill ->
                                    skillSynonymUtil
                                        .areSkillsRelated(
                                            seekerSkill,
                                            jobSkill)));
 
                if (hasMatch) {
                    recommendedJobs.add(job);
                }
            }
        }
 
        if (recommendedJobs.isEmpty()) {
            return allJobs;
        }
 
        // Module 7: Sort by match percentage
        // highest match shown first
        
        recommendedJobs.sort((job1, job2) -> {
            int match1 = calculateMatchPercentage(
                job1, seeker);
            int match2 = calculateMatchPercentage(
                job2, seeker);
            return Integer.compare(match2, match1);
        });

         
        return recommendedJobs;
    }
 
    // ─── AI MATCH PERCENTAGE ──────────────────
    // Module 7: Upgraded with semantic matching
    // and weighted scoring
 
    public int calculateMatchPercentage(
        JobListing job, User seeker) {
 
        Optional<SeekerProfile> profileOpt =
            seekerProfileRepository
                .findByUser(seeker);
 
        if (profileOpt.isEmpty()) return 0;
 
        String skills =
            profileOpt.get().getSkills();
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
 
        // Module 7: Count semantic matches
        // instead of exact string matches
        long matchCount = jobSkills.stream()
            .filter(jobSkill ->
                seekerSkills.stream()
                    .anyMatch(seekerSkill ->
                        skillSynonymUtil
                            .areSkillsRelated(
                                seekerSkill,
                                jobSkill)))
            .count();
 
        // Calculate percentage
        int skillScore = (int) (
            (matchCount * 100)
            / jobSkills.size());
 
        // Module 7: Title match bonus
        // If job title contains any seeker skill
        // add 10% bonus
        String jobTitle =
            job.getTitle().toLowerCase();
        boolean titleMatch = seekerSkills
            .stream()
            .anyMatch(skill ->
                jobTitle.contains(skill));
 
        if (titleMatch && skillScore < 100) {
            skillScore = Math.min(100,
                skillScore + 10);
        }
 
        return skillScore;
    }
}
 



























