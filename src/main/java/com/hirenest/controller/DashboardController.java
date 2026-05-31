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

        Optional<EmployerProfile> profileOpt =
            employerProfileRepository
                .findByUser(employer);
        
        
     
        // Module 6 fix — now uses isApproved
        // from admin approval instead of
        // just checking companyName
        boolean profileCompleted =
            profileOpt.isPresent()
            && profileOpt.get().isApproved();
        
        

        model.addAttribute("profileCompleted",
            profileCompleted);

        return "employer-dashboard";
    }

    // Admin dashboard now handled by
    // AdminController — no mapping here
}