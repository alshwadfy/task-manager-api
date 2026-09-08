package com.projects.Tasks.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.projects.Tasks.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final String fromEmail;

    public EmailServiceImpl(
            JavaMailSender javaMailSender,
            @Value("${spring.mail.username:}") String fromEmail
    ) {
        this.javaMailSender = javaMailSender;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendVerificationCode(String email, String verificationCode) {
        if (fromEmail == null || fromEmail.isBlank()) {
            log.error("MAIL_USERNAME is not configured. Cannot send verification email to {}. Set the MAIL_USERNAME and MAIL_PASSWORD environment variables.", email);
            throw new RuntimeException("Email service is not configured. Please contact support.");
        }
        try {
            log.info("Sending verification email to {} from {}", email, fromEmail);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("TaskFlow - Verify Your Email");
            message.setText(
                "Hello,\n\n" +
                "Your verification code is: " + verificationCode + "\n\n" +
                "This code expires in 24 hours.\n\n" +
                "If you did not register for TaskFlow, you can ignore this email.\n\n" +
                "- The TaskFlow Team"
            );
            javaMailSender.send(message);
            log.info("Verification email sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        }
    }
}