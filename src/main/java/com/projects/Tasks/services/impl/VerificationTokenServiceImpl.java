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

    private final UserRepo userRepository;
    private static final int EXPIRY_HOURS = 24;
    private static final Random random = new Random();

    public VerificationTokenServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateAndStoreVerificationCode(User user) {
        // Generate 6-digit code (000000 to 999999)
        String code = String.format("%06d", random.nextInt(1000000));
        
        // Store code and expiry time
        user.setVerificationCode(code);
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(EXPIRY_HOURS));
        
        // Save to database
        userRepository.save(user);
        
        return code;
    }

    @Override
    public boolean validateVerificationCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }

        // Find user
        User user = userRepository.findByEmail(email).orElse(null);
        
        if (user == null) {
            return false;
        }
        
        // Check if expired
        if (isVerificationCodeExpired(user)) {
            return false;
        }
        
        // Check if code matches
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(code)) {
            return false;
        }
        
        // Mark as verified
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
        
        return true;
    }

    @Override
    public boolean isVerificationCodeExpired(User user) {
        if (user == null || user.getVerificationCodeExpiresAt() == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(user.getVerificationCodeExpiresAt());
    }
}