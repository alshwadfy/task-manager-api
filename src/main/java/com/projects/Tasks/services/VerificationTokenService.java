package com.projects.Tasks.services;

import com.projects.Tasks.domain.entities.User;

public interface VerificationTokenService {
    String generateAndStoreVerificationCode(User user);
    boolean validateVerificationCode(String email, String code);
    boolean isVerificationCodeExpired(User user);
}