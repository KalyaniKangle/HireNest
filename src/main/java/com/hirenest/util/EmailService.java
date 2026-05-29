package com.hirenest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // ─── GENERIC EMAIL SENDER ─────────────────

    public void sendEmail(
        String toEmail,
        String subject,
        String body) {

        try {
            SimpleMailMessage message =
                new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(
                "noreply@hirenest.com");
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't crash app
            // if email fails
            System.out.println(
                "Email sending failed: "
                + e.getMessage());
        }
    }

    // ─── APPLICATION STATUS EMAILS ────────────

    // Email when application is received
    public void sendApplicationConfirmation(
        String seekerEmail,
        String seekerName,
        String jobTitle,
        String companyName) {

        String subject =
            "Application Received - "
            + jobTitle
            + " at "
            + companyName;

        String body =
            "Dear " + seekerName + ",\n\n"
            + "Your application has been successfully "
            + "submitted for the following position:\n\n"
            + "Job Title  : " + jobTitle + "\n"
            + "Company    : " + companyName + "\n"
            + "Status     : Applied\n\n"
            + "We will notify you as soon as your "
            + "application status is updated.\n\n"
            + "Best of luck!\n\n"
            + "Team HireNest\n"
            + "Where Careers Begin";

        sendEmail(seekerEmail, subject, body);
    }

    // Email when status changes to SHORTLISTED
    public void sendShortlistedEmail(
        String seekerEmail,
        String seekerName,
        String jobTitle,
        String companyName) {

        String subject =
            "🎉 Shortlisted! - "
            + jobTitle
            + " at "
            + companyName;

        String body =
            "Dear " + seekerName + ",\n\n"
            + "Congratulations! You have been "
            + "SHORTLISTED for the following position:\n\n"
            + "Job Title  : " + jobTitle + "\n"
            + "Company    : " + companyName + "\n"
            + "Status     : Shortlisted ✅\n\n"
            + "The employer will be in touch with "
            + "you soon regarding next steps.\n\n"
            + "Keep it up!\n\n"
            + "Team HireNest\n"
            + "Where Careers Begin";

        sendEmail(seekerEmail, subject, body);
    }

    // Email when status changes to SELECTED
    public void sendSelectedEmail(
        String seekerEmail,
        String seekerName,
        String jobTitle,
        String companyName) {

        String subject =
            "🏆 Congratulations! Selected - "
            + jobTitle
            + " at "
            + companyName;

        String body =
            "Dear " + seekerName + ",\n\n"
            + "We are thrilled to inform you that "
            + "you have been SELECTED for:\n\n"
            + "Job Title  : " + jobTitle + "\n"
            + "Company    : " + companyName + "\n"
            + "Status     : Selected 🏆\n\n"
            + "The employer will contact you shortly "
            + "with offer details and next steps.\n\n"
            + "Congratulations on this achievement!\n\n"
            + "Team HireNest\n"
            + "Where Careers Begin";

        sendEmail(seekerEmail, subject, body);
    }

    // Email when status changes to REJECTED
    public void sendRejectedEmail(
        String seekerEmail,
        String seekerName,
        String jobTitle,
        String companyName) {

        String subject =
            "Application Update - "
            + jobTitle
            + " at "
            + companyName;

        String body =
            "Dear " + seekerName + ",\n\n"
            + "Thank you for your interest in "
            + "the position below:\n\n"
            + "Job Title  : " + jobTitle + "\n"
            + "Company    : " + companyName + "\n"
            + "Status     : Not Selected\n\n"
            + "After careful consideration, the "
            + "employer has decided to move forward "
            + "with other candidates at this time.\n\n"
            + "Don't be discouraged! Keep applying "
            + "and the right opportunity will come.\n\n"
            + "Team HireNest\n"
            + "Where Careers Begin";

        sendEmail(seekerEmail, subject, body);
    }
}
