package com.hirenest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seeker_profile")
@Data
public class SeekerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String phone;
    private String location;

    @Column(length = 1000)
    private String skills;

    @Column(length = 1000)
    private String education;

    @Column(length = 1000)
    private String experience;

    @Column(length = 500)
    private String resumePath;

    @Column(length = 500)
    private String profileSummary;
}