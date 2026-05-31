package com.hirenest.controller;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.JobListing;
import com.hirenest.model.User;
import com.hirenest.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ─── ADMIN DASHBOARD ──────────────────────

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        // Platform statistics
        model.addAttribute("totalSeekers",
            adminService.getTotalSeekers());
        model.addAttribute("totalEmployers",
            adminService.getTotalEmployers());
        model.addAttribute("totalJobs",
            adminService.getTotalJobs());
        model.addAttribute("totalApplications",
            adminService.getTotalApplications());
        model.addAttribute("pendingEmployers",
            adminService.getPendingEmployersCount());
        model.addAttribute("activeJobs",
            adminService.getActiveJobs());

        // Pending employers list for
        // quick action on dashboard
        model.addAttribute("pendingList",
            adminService.getPendingEmployers());

        return "admin-dashboard";
    }

    // ─── EMPLOYER MANAGEMENT ──────────────────

    // View all employers
    @GetMapping("/admin/employers")
    public String viewEmployers(Model model) {

        List<EmployerProfile> employers =
            adminService.getAllEmployerProfiles();

        model.addAttribute("employers", employers);
        model.addAttribute("totalEmployers",
            employers.size());
        model.addAttribute("pendingCount",
            adminService.getPendingEmployers()
                .size());

        return "admin-employers";
    }

    // Approve employer
    @GetMapping("/admin/employers/approve/{profileId}")
    public String approveEmployer(
        @PathVariable Long profileId) {
        adminService.approveEmployer(profileId);
        return "redirect:/admin/employers"
            + "?approved=true";
    }

    // Reject employer
    @GetMapping("/admin/employers/reject/{profileId}")
    public String rejectEmployer(
        @PathVariable Long profileId) {
        adminService.rejectEmployer(profileId);
        return "redirect:/admin/employers"
            + "?rejected=true";
    }

    // Block user
    @GetMapping("/admin/users/block/{userId}")
    public String blockUser(
        @PathVariable Long userId) {
        adminService.blockUser(userId);
        return "redirect:/admin/employers"
            + "?blocked=true";
    }

    // Unblock user
    @GetMapping("/admin/users/unblock/{userId}")
    public String unblockUser(
        @PathVariable Long userId) {
        adminService.unblockUser(userId);
        return "redirect:/admin/employers"
            + "?unblocked=true";
    }

    // ─── SEEKER MANAGEMENT ────────────────────

    // View all seekers
    @GetMapping("/admin/seekers")
    public String viewSeekers(Model model) {

        List<User> seekers =
            adminService.getAllSeekers();

        model.addAttribute("seekers", seekers);
        model.addAttribute("totalSeekers",
            seekers.size());

        return "admin-seekers";
    }

    // Block seeker
    @GetMapping("/admin/seekers/block/{userId}")
    public String blockSeeker(
        @PathVariable Long userId) {
        adminService.blockUser(userId);
        return "redirect:/admin/seekers"
            + "?blocked=true";
    }

    // Unblock seeker
    @GetMapping("/admin/seekers/unblock/{userId}")
    public String unblockSeeker(
        @PathVariable Long userId) {
        adminService.unblockUser(userId);
        return "redirect:/admin/seekers"
            + "?unblocked=true";
    }

    // ─── JOB MANAGEMENT ───────────────────────

    // View all jobs
    @GetMapping("/admin/jobs")
    public String viewAllJobs(Model model) {

        List<JobListing> jobs =
            adminService.getAllJobs();

        model.addAttribute("jobs", jobs);
        model.addAttribute("totalJobs",
            jobs.size());

        return "admin-jobs";
    }

    // Remove a job
    @GetMapping("/admin/jobs/remove/{jobId}")
    public String removeJob(
        @PathVariable Long jobId) {
        adminService.removeJob(jobId);
        return "redirect:/admin/jobs"
            + "?removed=true";
    }
}
