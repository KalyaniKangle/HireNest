package com.hirenest.dto;

import com.hirenest.model.JobListing;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class JobListingRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Required skills is required")
    private String requiredSkills;

    @NotBlank(message = "Location is required")
    private String location;

    private String salaryRange;

    @NotNull(message = "Job type is required")
    private JobListing.JobType jobType;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;
}