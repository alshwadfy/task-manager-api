package com.projects.Tasks.Services.impl;

import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.repositories.UserRepo;
import com.projects.Tasks.services.impl.VerificationTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VerificationTokenServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private VerificationTokenServiceImpl verificationTokenService;

    @Test
    public void generateAndStoreVerificationCode_ShouldSetCodeAndExpiryAndSave() {
        User user = new User(UUID.randomUUID(), "ezz", "ezz@gmail.com", "pass", null, false);
        when(userRepo.save(any(User.class))).thenReturn(user);

        String code = verificationTokenService.generateAndStoreVerificationCode(user);

        assertNotNull(code);
        assertEquals(6, code.length());
        assertEquals(code, user.getVerificationCode());
        assertNotNull(user.getVerificationCodeExpiresAt());
        assertTrue(user.getVerificationCodeExpiresAt().isAfter(LocalDateTime.now()));
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void validateVerificationCode_ShouldReturnTrue_WhenCodeIsValid() {
        User user = new User(UUID.randomUUID(), "ezz", "ezz@gmail.com", "pass", null, false);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        when(userRepo.findByEmail("ezz@gmail.com")).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenReturn(user);

        boolean result = verificationTokenService.validateVerificationCode("ezz@gmail.com", "123456");

        assertTrue(result);
        assertTrue(user.getVerified());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiresAt());
        verify(userRepo, times(1)).save(user);
    }

    @Test
    public void validateVerificationCode_ShouldReturnFalse_WhenUserNotFound() {
        when(userRepo.findByEmail("notfound@gmail.com")).thenReturn(Optional.empty());

        boolean result = verificationTokenService.validateVerificationCode("notfound@gmail.com", "123456");

        assertFalse(result);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void validateVerificationCode_ShouldReturnFalse_WhenCodeIsExpired() {
        User user = new User(UUID.randomUUID(), "ezz", "ezz@gmail.com", "pass", null, false);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().minusHours(1));

        when(userRepo.findByEmail("ezz@gmail.com")).thenReturn(Optional.of(user));

        boolean result = verificationTokenService.validateVerificationCode("ezz@gmail.com", "123456");

        assertFalse(result);
        assertFalse(user.getVerified());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void validateVerificationCode_ShouldReturnFalse_WhenCodeDoesNotMatch() {
        User user = new User(UUID.randomUUID(), "ezz", "ezz@gmail.com", "pass", null, false);
        user.setVerificationCode("123456");
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));

        when(userRepo.findByEmail("ezz@gmail.com")).thenReturn(Optional.of(user));

        boolean result = verificationTokenService.validateVerificationCode("ezz@gmail.com", "999999");

        assertFalse(result);
        assertFalse(user.getVerified());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void validateVerificationCode_ShouldReturnFalse_WhenEmailOrCodeIsNull() {
        assertFalse(verificationTokenService.validateVerificationCode(null, "123456"));
        assertFalse(verificationTokenService.validateVerificationCode("ezz@gmail.com", null));
    }

    @Test
    public void isVerificationCodeExpired_ShouldReturnTrue_WhenNull() {
        assertTrue(verificationTokenService.isVerificationCodeExpired(null));

        User user = new User(UUID.randomUUID(), "ezz", "ezz@gmail.com", "pass", null, false);
        user.setVerificationCodeExpiresAt(null);
        assertTrue(verificationTokenService.isVerificationCodeExpired(user));
    }
}
