package com.hirenest.controller;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.EmployerProfileRepository;
import com.hirenest.security.CustomUserDetails;
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

    @GetMapping("/seeker/dashboard")
    public String seekerDashboard() {
        return "seeker-dashboard";
    }

    @GetMapping("/employer/dashboard")
    public String employerDashboard(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User employer = userDetails.getUser();

        // Find employer profile from database
        Optional<EmployerProfile> profileOpt =
            employerProfileRepository
                .findByUser(employer);

        // If profile exists check if
        // companyName is filled
        boolean profileCompleted =
            profileOpt.isPresent()
            && profileOpt.get().getCompanyName()
                != null
            && !profileOpt.get().getCompanyName()
                .trim().isEmpty();

        model.addAttribute("profileCompleted",
            profileCompleted);

        return "employer-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
}
