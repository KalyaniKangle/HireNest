package com.hirenest.controller;

import com.hirenest.dto.JobListingRequest;
import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import com.hirenest.security.CustomUserDetails;
import com.hirenest.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;

@Controller
public class JobController {

    @Autowired
    private JobService jobService;

    // ─── EMPLOYER CONTROLLERS ─────────────────

    // Show post job form
    @GetMapping("/employer/post-job")
    public String showPostJobForm(Model model) {
        model.addAttribute("jobRequest",
            new JobListingRequest());
        model.addAttribute("jobTypes",
            JobListing.JobType.values());
        return "post-job";
    }

    // Handle post job form submission
    @PostMapping("/employer/post-job")
    public String postJob(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        @Valid @ModelAttribute
        JobListingRequest jobRequest,
        BindingResult result,
        Model model) {

        if (result.hasErrors()) {
            model.addAttribute("jobTypes",
                JobListing.JobType.values());
            return "post-job";
        }

        User employer = userDetails.getUser();
        jobService.postJob(employer, jobRequest);
        return "redirect:/employer/jobs?posted=true";
    }

    // Show employer job listings
    @GetMapping("/employer/jobs")
    public String showEmployerJobs(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User employer = userDetails.getUser();
        List<JobListing> jobs =
            jobService.getEmployerJobs(employer);
        model.addAttribute("jobs", jobs);
        return "employer-jobs";
    }

    // Close a job listing
    @GetMapping("/employer/jobs/close/{jobId}")
    public String closeJob(
        @PathVariable Long jobId) {
        jobService.closeJob(jobId);
        return "redirect:/employer/jobs";
    }

    // Reopen a job listing
    @GetMapping("/employer/jobs/reopen/{jobId}")
    public String reopenJob(
        @PathVariable Long jobId) {
        jobService.reopenJob(jobId);
        return "redirect:/employer/jobs";
    }

    // ─── SEEKER CONTROLLERS ───────────────────

    // Browse all jobs with search and filters
    @GetMapping("/seeker/jobs")
    public String browseJobs(
        @RequestParam(required = false)
        String keyword,
        @RequestParam(required = false)
        String location,
        @RequestParam(required = false)
        String jobType,
        Model model) {

        List<JobListing> jobs =
            jobService.searchJobs(
                keyword, location, jobType);

        model.addAttribute("jobs", jobs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("location", location);
        model.addAttribute("jobType", jobType);
        model.addAttribute("jobTypes",
            JobListing.JobType.values());
        model.addAttribute("totalJobs",
            jobs.size());
        return "job-listings";
    }

    // View job detail
    @GetMapping("/seeker/jobs/{jobId}")
    public String viewJobDetail(
        @PathVariable Long jobId,
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        Optional<JobListing> jobOpt =
            jobService.getJobById(jobId);

        if (jobOpt.isEmpty()) {
            return "redirect:/seeker/jobs";
        }

        JobListing job = jobOpt.get();
        User seeker = userDetails.getUser();

        int matchPercent =
            jobService.calculateMatchPercentage(
                job, seeker);

        model.addAttribute("job", job);
        model.addAttribute("matchPercent",
            matchPercent);
        return "job-detail";
    }

    // Recommended jobs
    @GetMapping("/seeker/recommended")
    public String recommendedJobs(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User seeker = userDetails.getUser();
        List<JobListing> jobs =
            jobService.getRecommendedJobs(seeker);

        model.addAttribute("jobs", jobs);
        model.addAttribute("totalJobs",
            jobs.size());
        return "recommended-jobs";
    }
}