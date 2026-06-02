package com.hirenest.controller;

import com.hirenest.model.Application;
import com.hirenest.model.EmployerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.EmployerProfileRepository;
import com.hirenest.security.CustomUserDetails;
import com.hirenest.service.ApplicationService;
import com.hirenest.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private EmployerProfileRepository
        employerProfileRepository;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;

    // ─── SEEKER DASHBOARD ─────────────────────

    @GetMapping("/seeker/dashboard")
    public String seekerDashboard(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User seeker = userDetails.getUser();

        // Real stats from database
        long totalApplications =
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

        long jobMatches =
            jobService.getRecommendedJobs(seeker)
                .size();

        model.addAttribute("totalApplications",
            totalApplications);
        model.addAttribute("shortlisted",
            shortlisted);
        model.addAttribute("selected", selected);
        model.addAttribute("jobMatches",
            jobMatches);

        return "seeker-dashboard";
    }

    // ─── EMPLOYER DASHBOARD ───────────────────

    @GetMapping("/employer/dashboard")
    public String employerDashboard(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User employer = userDetails.getUser();

        // Approval status
        Optional<EmployerProfile> profileOpt =
            employerProfileRepository
                .findByUser(employer);

        boolean profileCompleted =
            profileOpt.isPresent()
            && profileOpt.get().isApproved();

        model.addAttribute("profileCompleted",
            profileCompleted);

        // Real stats from database
        long totalJobs =
            jobService.getEmployerJobs(employer)
                .size();

        long totalApplicants =
            applicationService
                .countTotalApplicants(employer);

        long pendingReview =
            applicationService
                .countEmployerByStatus(employer,
                Application.Status.APPLIED);

        long shortlisted =
            applicationService
                .countEmployerByStatus(employer,
                Application.Status.SHORTLISTED);

        model.addAttribute("totalJobs",
            totalJobs);
        model.addAttribute("totalApplicants",
            totalApplicants);
        model.addAttribute("pendingReview",
            pendingReview);
        model.addAttribute("shortlisted",
            shortlisted);

        return "employer-dashboard";
    }

    // Admin dashboard handled by AdminController
}
