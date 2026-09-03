package com.projects.Tasks.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.repositories.UserRepo;
import com.projects.Tasks.services.VerificationTokenService;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private UserRepo userRepository;

    private static final int CODE_LENGTH = 6;
    private static final int EXPIRY_HOURS = 24;
    private static final Random random = new Random();

    @Override
    public String generateAndStoreVerificationCode(User user) {
        // Generate 6-digit code (000000 to 999999)
        String code = String.format("%06d", random.nextInt(1000000));
        
        // Store code and expiry time
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(EXPIRY_HOURS));
        
        // Save to database
        userRepository.save(user);
        
        System.out.println("✅ Generated code for " + user.getEmail() + ": " + code);
        
        return code;
    }

    @Override
    public boolean validateVerificationCode(String email, String code) {
        // Find user
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user == null) {
            System.err.println("❌ User not found: " + email);
            return false;
        }
        
        // Check if expired
        if (isVerificationCodeExpired(user)) {
            System.err.println("❌ Code expired for: " + email);
            return false;
        }
        
        // Check if code matches
        if (!user.getVerificationCode().equals(code)) {
            System.err.println("❌ Invalid code for: " + email);
            return false;
        }
        
        // Mark as verified
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
        
        System.out.println("✅ Email verified for: " + email);
        
        return true;
    }

    @Override
    public boolean isVerificationCodeExpired(User user) {
        if (user.getVerificationCodeExpiresAt() == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(user.getVerificationCodeExpiresAt());
    }
}