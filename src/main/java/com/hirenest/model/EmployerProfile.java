package com.hirenest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employer_profile")
@Data
public class EmployerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 2000)
    private String companyDescription;

    private String website;
    private String location;
    private String industry;
    private String companySize;
    private boolean isApproved = false;
}