package com.hirenest.service;

import com.hirenest.dto.RegisterRequest;
import com.hirenest.model.User;
import com.hirenest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(
            request.getEmail())) {
            return "Email already registered. " +
                "Please login.";
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
            passwordEncoder.encode(
                request.getPassword())
        );
        user.setRole(request.getRole());
        user.setActive(true);

        // Save to database
        userRepository.save(user);

        return "success";
    }

    public User findByEmail(String email) {
        return userRepository
            .findByEmail(email)
            .orElse(null);
    }
}