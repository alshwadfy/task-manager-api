package com.projects.Tasks.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.projects.Tasks.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationCode(String email, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("TaskFlow - Verify Your Email");
            message.setText("Your verification code is: " + verificationCode + "\n\nThis code expires in 24 hours.");
            
            javaMailSender.send(message);
            System.out.println(" Email sent to: " + email);
        } catch (Exception e) {
            System.err.println(" Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}