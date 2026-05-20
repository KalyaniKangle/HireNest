package com.hirenest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_listing")
@Data
public class JobListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String requiredSkills;
    private String location;
    private String salaryRange;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private LocalDate deadline;
    private boolean isActive = true;
    private LocalDateTime postedAt = LocalDateTime.now();

    public enum JobType {
        FULL_TIME, PART_TIME, INTERNSHIP, REMOTE
    }
}