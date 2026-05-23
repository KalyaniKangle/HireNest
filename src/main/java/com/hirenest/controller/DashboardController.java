package com.hirenest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/seeker/dashboard")
    public String seekerDashboard() {
        return "seeker-dashboard";
    }

    @GetMapping("/employer/dashboard")
    public String employerDashboard() {
        return "employer-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
}