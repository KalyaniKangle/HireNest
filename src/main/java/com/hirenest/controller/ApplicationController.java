package com.hirenest.controller;

import com.hirenest.model.Application;
import com.hirenest.model.User;
import com.hirenest.security.CustomUserDetails;
import com.hirenest.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // ─── SEEKER CONTROLLERS ───────────────────

    // Apply for a job — POST
    @PostMapping("/seeker/apply/{jobId}")
    public String applyForJob(
        @PathVariable Long jobId,
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        @RequestParam(required = false)
        String coverLetter) {

        User seeker = userDetails.getUser();

        String result =
            applicationService.applyForJob(
                seeker, jobId, coverLetter);

        if (result.equals("already_applied")) {
            return "redirect:/seeker/jobs/"
                + jobId
                + "?alreadyApplied=true";
        }

        if (result.equals("success")) {
            return "redirect:/seeker/jobs/"
                + jobId
                + "?applied=true";
        }

        // Job not found or closed
        return "redirect:/seeker/jobs"
            + "?error=true";
    }

    // Show apply form — GET
    @GetMapping("/seeker/apply/{jobId}")
    public String showApplyForm(
        @PathVariable Long jobId,
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User seeker = userDetails.getUser();

        // Check if already applied
        if (applicationService
            .hasApplied(seeker, jobId)) {
            return "redirect:/seeker/jobs/"
                + jobId
                + "?alreadyApplied=true";
        }

        model.addAttribute("jobId", jobId);
        return "apply-job";
    }

    // My Applications — seeker tracks all
    @GetMapping("/seeker/applications")
    public String myApplications(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User seeker = userDetails.getUser();

        List<Application> applications =
            applicationService
                .getSeekerApplications(seeker);

        // Stats for dashboard
        long totalApplied =
            applicationService
                .countApplications(seeker);
        long shortlisted =
            applicationService.countByStatus(
                seeker,
                Application.Status.SHORTLISTED);
        long selected =
            applicationService.countByStatus(
                seeker,
                Application.Status.SELECTED);
        long rejected =
            applicationService.countByStatus(
                seeker,
                Application.Status.REJECTED);

        model.addAttribute("applications",
            applications);
        model.addAttribute("totalApplied",
            totalApplied);
        model.addAttribute("shortlisted",
            shortlisted);
        model.addAttribute("selected", selected);
        model.addAttribute("rejected", rejected);

        return "my-applications";
    }

    // ─── EMPLOYER CONTROLLERS ─────────────────

    // View all applicants for a specific job
    @GetMapping("/employer/applications/{jobId}")
    public String viewJobApplicants(
        @PathVariable Long jobId,
        Model model) {

        List<Application> applications =
            applicationService
                .getJobApplicants(jobId);

        model.addAttribute("applications",
            applications);
        model.addAttribute("jobId", jobId);
        model.addAttribute("totalApplicants",
            applications.size());

        return "employer-applicants";
    }

    // View all applicants across all jobs
    @GetMapping("/employer/applicants")
    public String viewAllApplicants(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User employer = userDetails.getUser();

        List<Application> applications =
            applicationService
                .getAllEmployerApplicants(employer);

        // Stats
        long total =
            applicationService
                .countTotalApplicants(employer);
        long shortlisted =
            applicationService
                .countEmployerByStatus(employer,
                Application.Status.SHORTLISTED);
        long selected =
            applicationService
                .countEmployerByStatus(employer,
                Application.Status.SELECTED);
        long rejected =
            applicationService
                .countEmployerByStatus(employer,
                Application.Status.REJECTED);

        model.addAttribute("applications",
            applications);
        model.addAttribute("total", total);
        model.addAttribute("shortlisted",
            shortlisted);
        model.addAttribute("selected", selected);
        model.addAttribute("rejected", rejected);

        return "employer-applicants";
    }

    // Update application status — employer action
    @PostMapping("/employer/applications/update-status")
    public String updateStatus(
        @RequestParam Long applicationId,
        @RequestParam String status,
        @RequestParam(required = false)
        Long jobId) {

        Application.Status newStatus =
            Application.Status.valueOf(status);

        applicationService.updateStatus(
            applicationId, newStatus);

        // Redirect back to job applicants
        // if jobId present else all applicants
        if (jobId != null) {
            return "redirect:/employer/applications/"
                + jobId
                + "?updated=true";
        }

        return "redirect:/employer/applicants"
            + "?updated=true";
    }
}
