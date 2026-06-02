package com.hirenest.controller;

import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.SeekerProfileRepository;
import com.hirenest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class ResumeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeekerProfileRepository
        seekerProfileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Employer downloads seeker resume
    @GetMapping("/employer/resume/download/{seekerId}")
    public ResponseEntity<Resource>
        downloadResume(
        @PathVariable Long seekerId) {

        try {
            // Find seeker
            Optional<User> userOpt =
                userRepository
                    .findById(seekerId);

            if (userOpt.isEmpty()) {
                return ResponseEntity
                    .notFound().build();
            }

            // Find seeker profile
            Optional<SeekerProfile> profileOpt =
                seekerProfileRepository
                    .findByUser(userOpt.get());

            if (profileOpt.isEmpty()
                || profileOpt.get()
                    .getResumePath() == null
                || profileOpt.get()
                    .getResumePath().isEmpty()) {
                return ResponseEntity
                    .notFound().build();
            }

            String resumePath =
                profileOpt.get().getResumePath();

            // Build file path
            Path filePath = Paths.get(
                uploadDir).resolve(
                Paths.get(resumePath)
                    .getFileName().toString())
                .normalize();

            Resource resource =
                new UrlResource(
                    filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity
                    .notFound().build();
            }

            // Get seeker name for filename
            String seekerName =
                userOpt.get().getName()
                    .replace(" ", "_");

            String downloadFilename =
                "Resume_" + seekerName + ".pdf";

            return ResponseEntity.ok()
                .contentType(
                    MediaType.APPLICATION_PDF)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\""
                    + downloadFilename + "\"")
                .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity
                .badRequest().build();
        }
    }
}
