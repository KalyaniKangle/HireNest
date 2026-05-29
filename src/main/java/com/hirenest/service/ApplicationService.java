package com.hirenest.service;

import com.hirenest.model.Application;
import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import com.hirenest.repository.ApplicationRepository;
import com.hirenest.repository.JobListingRepository;
import com.hirenest.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository
        applicationRepository;

    @Autowired
    private JobListingRepository
        jobListingRepository;

    @Autowired
    private EmailService emailService;

    // ─── SEEKER METHODS ───────────────────────

    // Apply for a job
    public String applyForJob(
        User seeker, Long jobId,
        String coverLetter) {

        // Find the job
        Optional<JobListing> jobOpt =
            jobListingRepository.findById(jobId);

        if (jobOpt.isEmpty()) {
            return "Job not found";
        }

        JobListing job = jobOpt.get();

        // Check if job is still active
        if (!job.isActive()) {
            return "This job is no longer accepting applications";
        }

        // Check if already applied
        if (applicationRepository
            .existsBySeekerAndJob(seeker, job)) {
            return "already_applied";
        }

        // Create new application
        Application application =
            new Application();
        application.setSeeker(seeker);
        application.setJob(job);
        application.setCoverLetter(coverLetter);
        application.setStatus(
            Application.Status.APPLIED);

        applicationRepository.save(application);

        // Send confirmation email to seeker
        emailService.sendApplicationConfirmation(
            seeker.getEmail(),
            seeker.getName(),
            job.getTitle(),
            job.getEmployer().getName());

        return "success";
    }

    // Get all applications by seeker
    public List<Application> getSeekerApplications(
        User seeker) {
        return applicationRepository
            .findBySeekerOrderByAppliedAtDesc(
                seeker);
    }

    // Check if seeker already applied
    public boolean hasApplied(
        User seeker, Long jobId) {

        Optional<JobListing> jobOpt =
            jobListingRepository.findById(jobId);

        if (jobOpt.isEmpty()) return false;

        return applicationRepository
            .existsBySeekerAndJob(
                seeker, jobOpt.get());
    }

    // Get seeker stats for dashboard
    public long countApplications(User seeker) {
        return applicationRepository
            .countBySeeker(seeker);
    }

    public long countByStatus(
        User seeker,
        Application.Status status) {
        return applicationRepository
            .countBySeekerAndStatus(
                seeker, status);
    }

    // ─── EMPLOYER METHODS ─────────────────────

    // Get all applicants for a specific job
    public List<Application> getJobApplicants(
        Long jobId) {

        Optional<JobListing> jobOpt =
            jobListingRepository.findById(jobId);

        if (jobOpt.isEmpty()) return List.of();

        return applicationRepository
            .findByJobOrderByAppliedAtDesc(
                jobOpt.get());
    }

    // Get all applicants across all employer jobs
    public List<Application> getAllEmployerApplicants(
        User employer) {
        return applicationRepository
            .findByEmployer(employer);
    }

    // Update application status
    // and send email notification
    public void updateStatus(
        Long applicationId,
        Application.Status newStatus) {

        applicationRepository
            .findById(applicationId)
            .ifPresent(application -> {

                application.setStatus(newStatus);
                applicationRepository
                    .save(application);

                // Get details for email
                String seekerEmail =
                    application.getSeeker()
                        .getEmail();
                String seekerName =
                    application.getSeeker()
                        .getName();
                String jobTitle =
                    application.getJob()
                        .getTitle();
                String companyName =
                    application.getJob()
                        .getEmployer().getName();

                // Send email based on new status
                switch (newStatus) {
                    case SHORTLISTED ->
                        emailService
                            .sendShortlistedEmail(
                                seekerEmail,
                                seekerName,
                                jobTitle,
                                companyName);
                    case SELECTED ->
                        emailService
                            .sendSelectedEmail(
                                seekerEmail,
                                seekerName,
                                jobTitle,
                                companyName);
                    case REJECTED ->
                        emailService
                            .sendRejectedEmail(
                                seekerEmail,
                                seekerName,
                                jobTitle,
                                companyName);
                    default -> {
                        // No email for APPLIED status
                    }
                }
            });
    }

    // Get application by ID
    public Optional<Application> getApplicationById(
        Long applicationId) {
        return applicationRepository
            .findById(applicationId);
    }

    // Get employer stats
    public long countTotalApplicants(
        User employer) {
        return applicationRepository
            .countByEmployer(employer);
    }

    public long countEmployerByStatus(
        User employer,
        Application.Status status) {
        return applicationRepository
            .countByEmployerAndStatus(
                employer, status);
    }
}
