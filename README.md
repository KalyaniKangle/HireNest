# HireNest — Where Careers Begin

A full-stack job portal that solves real hiring problems through verified employers, real-time application tracking, and AI-powered skill matching.

---

## The Problem

Today's job market has serious problems that platforms like Naukri, LinkedIn and Indeed have not fully solved:

| Problem | Reality |
|---------|---------|
| Fake Job Postings | Anyone can post a job without verification |
| Zero Transparency | After applying, candidates hear nothing back |
| Poor Skill Matching | Random jobs shown regardless of your skills |
| Freshers Get Lost | LinkedIn favors connections over skills |
| Expensive for Small Companies | Naukri charges heavily for job postings |

---

## How HireNest Solves This

| Problem | HireNest Solution |
|---------|------------------|
| Fake Jobs | Every employer manually verified by admin before posting |
| Zero Transparency | Real-time status: Applied → Shortlisted → Selected/Rejected |
| Poor Matching | AI-powered semantic skill matching engine |
| Freshers Get Lost | Equal visibility based purely on skills, not connections |
| Expensive | Completely free platform for all job seekers |

---

## Project Overview

HireNest is a production-ready web-based job portal built with Spring Boot and MySQL. It supports three user roles — Job Seeker, Employer, and Admin — each with a dedicated dashboard and feature set. The platform features JWT-based authentication, role-based access control, email notifications, and an AI-powered recommendation engine.

---

## Key Features

**Job Seeker**
- Register and build a complete profile with skills and resume
- Browse all active job listings with search and filters
- Get AI-powered job recommendations based on skill matching
- View skill match percentage for each job
- Apply with cover letter in one click
- Track application status in real time
- Receive email notifications on every status change

**Employer**
- Register and complete company profile
- Post job listings with required skills and deadline
- Manage all job listings (close/reopen)
- View all applicants for each job
- Download applicant resumes (PDF)
- Update application status (Shortlist/Select/Reject)
- Email notifications sent to seekers automatically

**Admin**
- Manually verify and approve employer accounts
- Block or unblock any user
- Monitor all job listings and remove inappropriate ones
- View platform-wide statistics dashboard

---

## AI-Powered Skill Matching Engine

HireNest uses a Semantic Skill Matching Algorithm that goes beyond simple keyword search:

```
Traditional matching:  "Java" only matches "Java"

HireNest AI matching:  "Java" also matches
                       → Spring Boot (Java framework)
                       → J2EE (Java enterprise)
                       → Hibernate (Java ORM)
                       → Maven (Java build tool)
```

**How it works:**
1. Seeker adds skills to their profile
2. Engine expands each skill using synonym mapping
3. Expanded skills compared against job requirements
4. Match percentage calculated with title bonus scoring
5. Jobs sorted by match percentage — highest first

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3.x |
| Security | Spring Security, JWT (JSON Web Token) |
| Database | MySQL 8.0, Spring Data JPA, Hibernate |
| Frontend | Thymeleaf, Bootstrap 5, Bootstrap Icons |
| Email | JavaMailSender, Gmail SMTP |
| Build Tool | Maven |

---

## Project Structure

```
src/main/java/com/hirenest/
├── controller/
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── JobController.java
│   ├── ApplicationController.java
│   ├── AdminController.java
│   └── ResumeController.java
├── model/
│   ├── User.java
│   ├── JobListing.java
│   ├── Application.java
│   ├── SeekerProfile.java
│   └── EmployerProfile.java
├── repository/
│   ├── UserRepository.java
│   ├── JobListingRepository.java
│   ├── ApplicationRepository.java
│   ├── SeekerProfileRepository.java
│   └── EmployerProfileRepository.java
├── service/
│   ├── UserService.java
│   ├── JobService.java
│   ├── ApplicationService.java
│   └── AdminService.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtFilter.java
│   ├── SecurityConfig.java
│   ├── CustomUserDetails.java
│   └── CustomUserDetailsService.java
├── dto/
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   └── JobListingRequest.java
└── util/
    ├── EmailService.java
    └── SkillSynonymUtil.java
```

---

## Database Schema

```
users
├── user_id (PK)
├── name, email, password
├── role (SEEKER/EMPLOYER/ADMIN)
└── is_active, created_at

seeker_profile
├── profile_id (PK)
├── user_id (FK → users)
├── phone, location, skills
├── education, experience
└── resume_path, profile_summary

employer_profile
├── profile_id (PK)
├── user_id (FK → users)
├── company_name, company_description
├── website, location, industry
└── company_size, is_approved

job_listing
├── job_id (PK)
├── employer_id (FK → users)
├── title, description, required_skills
├── location, salary_range, job_type
└── deadline, is_active, posted_at

application
├── application_id (PK)
├── job_id (FK → job_listing)
├── seeker_id (FK → users)
├── cover_letter, status
└── applied_at
```

---

## Security Architecture

```
Every request → JwtFilter
               → Reads JWT from HTTP-only cookie
               → Validates token signature
               → Sets authentication in SecurityContext

Role Based Access:
/seeker/**   → SEEKER role only
/employer/** → EMPLOYER role only
/admin/**    → ADMIN role only
/login, /register → public
```

---

## Modules Built

| Module | Description |
|--------|-------------|
| Module 1 | Project setup, entity classes, database configuration |
| Module 2 | JWT authentication, Spring Security, role-based access |
| Module 3 | Seeker and employer profile management |
| Module 4 | Job posting, search, filters, recommendations |
| Module 5 | Application management, email notifications |
| Module 6 | Admin panel, employer approval, platform management |
| Module 7 | AI semantic skill matching, resume download |

---

## How to Run Locally

**Prerequisites**
- Java 17+
- MySQL 8.0+
- Maven 3.6+

**Steps**

1. Clone the repository
```bash
git clone https://github.com/KalyaniKangle/hirenest.git
cd hirenest
```

2. Create MySQL database
```sql
CREATE DATABASE hirenest_db;
```

3. Configure `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hirenest_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.mail.username=YOUR_GMAIL@gmail.com
spring.mail.password=YOUR_16_DIGIT_APP_PASSWORD
```

4. Run the application
```bash
mvn spring-boot:run
```

5. Open browser at `http://localhost:9090`

---

## Application Flow

```
Seeker applies for job
        ↓
Email confirmation sent to seeker
        ↓
Employer reviews application
        ↓
Employer updates status (Shortlisted/Selected/Rejected)
        ↓
Automatic email sent to seeker
        ↓
Seeker tracks real-time status in dashboard
```

---

## About the Developer

**Kalyani Kangle**  
B.E. Computer Science and Engineering  
MGICOET Shegaon  

Full Stack Developer — Java, Spring Boot, MySQL, JWT, Spring Security, Bootstrap 5, Thymeleaf, Maven(Build Tool)

---

*This project is built for educational and portfolio purposes.*
