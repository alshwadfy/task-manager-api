package com.projects.Tasks.services;

public interface EmailService {
    void sendVerificationCode(String email, String verificationCode);
}
//implements SMTPEmailService in the future to add more email providers
