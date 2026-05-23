package com.hirenest.service;

import com.hirenest.model.EmployerProfile;
import com.hirenest.model.SeekerProfile;
import com.hirenest.model.User;
import com.hirenest.repository.EmployerProfileRepository;
import com.hirenest.repository.SeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ProfileService {

    @Autowired
    private SeekerProfileRepository seekerProfileRepository;

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ─── SEEKER PROFILE ───────────────────────

    public SeekerProfile getSeekerProfile(User user) {
        return seekerProfileRepository
            .findByUser(user)
            .orElse(new SeekerProfile());
    }

    public void saveSeekerProfile(
        User user,
        SeekerProfile profile,
        MultipartFile resumeFile) throws IOException {

        // Check if profile already exists
        SeekerProfile existing =
            seekerProfileRepository
                .findByUser(user)
                .orElse(new SeekerProfile());

        // Set user
        existing.setUser(user);
        existing.setPhone(profile.getPhone());
        existing.setLocation(profile.getLocation());
        existing.setSkills(profile.getSkills());
        existing.setEducation(profile.getEducation());
        existing.setExperience(profile.getExperience());
        existing.setProfileSummary(
            profile.getProfileSummary());

        // Handle resume file upload
        if (resumeFile != null
            && !resumeFile.isEmpty()) {

            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file with unique name
            String fileName = user.getUserId()
                + "_" + resumeFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(resumeFile.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

            existing.setResumePath(
                uploadDir + "/" + fileName);
        }

        seekerProfileRepository.save(existing);
    }

    // ─── EMPLOYER PROFILE ─────────────────────

    public EmployerProfile getEmployerProfile(
        User user) {
        return employerProfileRepository
            .findByUser(user)
            .orElse(new EmployerProfile());
    }

    public void saveEmployerProfile(
        User user,
        EmployerProfile profile) {

        // Check if profile already exists
        EmployerProfile existing =
            employerProfileRepository
                .findByUser(user)
                .orElse(new EmployerProfile());

        // Set user and profile details
        existing.setUser(user);
        existing.setCompanyName(
            profile.getCompanyName());
        existing.setCompanyDescription(
            profile.getCompanyDescription());
        existing.setWebsite(profile.getWebsite());
        existing.setLocation(profile.getLocation());
        existing.setIndustry(profile.getIndustry());
        existing.setCompanySize(
            profile.getCompanySize());

        // Keep existing approval status
        // Admin approval is separate process
        employerProfileRepository.save(existing);
    }
}