package com.hirenest.controller;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import com.hirenest.security.CustomUserDetails;
import com.hirenest.service.ProfileService;
import com.hirenest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // ─── SEEKER PROFILE ───────────────────────

    // Show seeker profile page
    @GetMapping("/seeker/profile")
    public String showSeekerProfile(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User user = userDetails.getUser();
        SeekerProfile profile =
            profileService.getSeekerProfile(user);

        model.addAttribute("profile", profile);
        model.addAttribute("user", user);
        return "seeker-profile";
    }

    // Save seeker profile
    @PostMapping("/seeker/profile")
    public String saveSeekerProfile(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        @ModelAttribute SeekerProfile profile,
        @RequestParam(value = "resumeFile",
            required = false)
        MultipartFile resumeFile,
        Model model) {

        try {
            User user = userDetails.getUser();
            profileService.saveSeekerProfile(
                user, profile, resumeFile);
            model.addAttribute("success",
                "Profile saved successfully!");
        } catch (Exception e) {
            model.addAttribute("error",
                "Error saving profile. " +
                "Please try again.");
        }

        return "redirect:/seeker/profile?saved=true";
    }

    // ─── EMPLOYER PROFILE ─────────────────────

    // Show employer profile page
    @GetMapping("/employer/profile")
    public String showEmployerProfile(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        Model model) {

        User user = userDetails.getUser();
        EmployerProfile profile =
            profileService.getEmployerProfile(user);

        model.addAttribute("profile", profile);
        model.addAttribute("user", user);
        return "employer-profile";
    }

    // Save employer profile
    @PostMapping("/employer/profile")
    public String saveEmployerProfile(
        @AuthenticationPrincipal
        CustomUserDetails userDetails,
        @ModelAttribute EmployerProfile profile,
        Model model) {

        try {
            User user = userDetails.getUser();
            profileService.saveEmployerProfile(
                user, profile);
        } catch (Exception e) {
            model.addAttribute("error",
                "Error saving profile. " +
                "Please try again.");
        }

        return "redirect:/employer/profile?saved=true";
    }
}