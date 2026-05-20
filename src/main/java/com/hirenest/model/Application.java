package com.hirenest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobListing job;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    private User seeker;

    @Column(length = 1000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private Status status = Status.APPLIED;

    private LocalDateTime appliedAt = LocalDateTime.now();

    public enum Status {
        APPLIED, SHORTLISTED, REJECTED, SELECTED
    }
}