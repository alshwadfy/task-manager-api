package com.projects.Tasks.Controller;

import com.projects.Tasks.domain.dto.AuthResponse;
import com.projects.Tasks.domain.dto.LoginRequest;
import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.domain.dto.ResendVerificationRequest;
import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.dto.VerificationRequest;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.UserMapper;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.EmailService;
import com.projects.Tasks.services.UserService;
import com.projects.Tasks.services.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    public AuthController(
            UserService userService,
            UserMapper userMapper,
            JwtService jwtService,
            VerificationTokenService verificationTokenService,
            EmailService emailService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        User created = userService.createUser(registerRequest);
        String code = verificationTokenService.generateAndStoreVerificationCode(created);

        Map<String, Object> response = new HashMap<>();
        response.put("user", userMapper.toDto(created));

        try {
            emailService.sendVerificationCode(created.getEmail(), code);
            response.put("emailSent", true);
            response.put("message", "Registration successful! A verification code has been sent to " + created.getEmail() + ". Please verify your email to log in.");
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", created.getEmail(), e.getMessage(), e);
            response.put("emailSent", false);
            response.put("message", "Registration successful! However, we could not send a verification email. Please use 'Resend Verification' or contact support.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestBody VerificationRequest verificationRequest) {
        if (verificationRequest.email() == null || verificationRequest.email().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        if (verificationRequest.code() == null || verificationRequest.code().isBlank()) {
            throw new IllegalArgumentException("verification code is required");
        }
        boolean isVerified = verificationTokenService.validateVerificationCode(
                verificationRequest.email(),
                verificationRequest.code()
        );
        if (!isVerified) {
            throw new IllegalArgumentException("Invalid or expired verification code");
        }
        return ResponseEntity.ok(Map.of("message", "Email verified successfully! You can now log in."));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, String>> resendVerification(@RequestBody ResendVerificationRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("email is required");
        }
        User user = userService.getUserByEmail(request.email());
        if (Boolean.TRUE.equals(user.getVerified())) {
            throw new IllegalArgumentException("Email is already verified");
        }
        String code = verificationTokenService.generateAndStoreVerificationCode(user);
        emailService.sendVerificationCode(user.getEmail(), code);
        return ResponseEntity.ok(Map.of("message", "Verification code sent successfully"));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.email(), loginRequest.password());
        String token = jwtService.generateToken(user.getUserId());
        return new AuthResponse(token, user.getUserId(), user.getUsername(), user.getEmail());
    }
}