package com.hirenest.controller;

import com.hirenest.dto.LoginRequest;
import com.hirenest.dto.RegisterRequest;
import com.hirenest.model.User;
import com.hirenest.security.JwtUtil;
import com.hirenest.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(
        @Valid @ModelAttribute RegisterRequest request,
        BindingResult result,
        Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        String response =
            userService.registerUser(request);

        if (response.equals("success")) {
            return "redirect:/login?registered=true";
        } else {
            model.addAttribute("error", response);
            return "register";
        }
    }

    // Handle login form submission
    @PostMapping("/login")
    public String loginUser(
        @Valid @ModelAttribute LoginRequest request,
        BindingResult result,
        HttpServletResponse response,
        Model model) {

        if (result.hasErrors()) {
            return "login";
        }

        try {
            // Authenticate user
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            // Get user details
            User user = userService
                .findByEmail(request.getEmail());

            // Generate JWT token
            String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
            );

            // Store token in cookie
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            // Redirect based on role
            switch (user.getRole()) {
                case SEEKER:
                    return "redirect:/seeker/dashboard";
                case EMPLOYER:
                    return "redirect:/employer/dashboard";
                case ADMIN:
                    return "redirect:/admin/dashboard";
                default:
                    return "redirect:/login";
            }

        } catch (BadCredentialsException e) {
            model.addAttribute("error",
                "Invalid email or password");
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/login?logout=true";
    }
}