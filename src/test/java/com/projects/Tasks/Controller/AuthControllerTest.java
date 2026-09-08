package com.projects.Tasks.Controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private VerificationTokenService verificationTokenService;

    @MockitoBean
    private EmailService emailService;

    @Test
    public void login_ShouldReturn200AndAuthResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest("ezz@example.com", "password");
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            "ezz",
            "ezz@example.com",
            "Hashedpassword",
            null,
            true
        );
        when(userService.login(loginRequest.email(), loginRequest.password())).thenReturn(user);
        when(jwtService.generateToken(userId)).thenReturn("token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.username").value("ezz"))
                .andExpect(jsonPath("$.email").value("ezz@example.com"));
    }

    @Test
    public void register_ShouldReturn200AndUserDto() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("ezz", "ezz@example.com", "password");
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "ezz", "ezz@example.com", "Hashedpassword", null, false);
        UserDto userDto = new UserDto(userId, "ezz", "ezz@example.com", false, null);

        when(userService.createUser(any(RegisterRequest.class))).thenReturn(user);
        when(verificationTokenService.generateAndStoreVerificationCode(user)).thenReturn("123456");
        doNothing().when(emailService).sendVerificationCode("ezz@example.com", "123456");
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ezz"))
                .andExpect(jsonPath("$.email").value("ezz@example.com"))
                .andExpect(jsonPath("$.verified").value(false));

        verify(verificationTokenService).generateAndStoreVerificationCode(user);
        verify(emailService).sendVerificationCode("ezz@example.com", "123456");
    }

    @Test
    public void verify_ShouldReturn200_WhenCodeIsValid() throws Exception {
        VerificationRequest request = new VerificationRequest("ezz@example.com", "123456");
        when(verificationTokenService.validateVerificationCode("ezz@example.com", "123456")).thenReturn(true);

        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email verified successfully"));
    }

    @Test
    public void verify_ShouldReturn400_WhenCodeIsInvalid() throws Exception {
        VerificationRequest request = new VerificationRequest("ezz@example.com", "000000");
        when(verificationTokenService.validateVerificationCode("ezz@example.com", "000000")).thenReturn(false);

        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid or expired verification code"));
    }

    @Test
    public void verify_ShouldReturn400_WhenEmailIsMissing() throws Exception {
        VerificationRequest request = new VerificationRequest("", "123456");

        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email is required"));
    }

    @Test
    public void resendVerification_ShouldReturn200_WhenValid() throws Exception {
        ResendVerificationRequest request = new ResendVerificationRequest("ezz@example.com");
        User user = new User(UUID.randomUUID(), "ezz", "ezz@example.com", "hashed", null, false);

        when(userService.getUserByEmail("ezz@example.com")).thenReturn(user);
        when(verificationTokenService.generateAndStoreVerificationCode(user)).thenReturn("654321");
        doNothing().when(emailService).sendVerificationCode("ezz@example.com", "654321");

        mockMvc.perform(post("/api/auth/resend-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Verification code sent successfully"));

        verify(verificationTokenService).generateAndStoreVerificationCode(user);
        verify(emailService).sendVerificationCode("ezz@example.com", "654321");
    }

    @Test
    public void resendVerification_ShouldReturn400_WhenAlreadyVerified() throws Exception {
        ResendVerificationRequest request = new ResendVerificationRequest("ezz@example.com");
        User user = new User(UUID.randomUUID(), "ezz", "ezz@example.com", "hashed", null, true);

        when(userService.getUserByEmail("ezz@example.com")).thenReturn(user);

        mockMvc.perform(post("/api/auth/resend-verification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is already verified"));
    }

    @Test
    public void login_ShouldReturn400_WhenCredintialsIsInvalid() throws Exception {
        LoginRequest loginRequest = new LoginRequest("ezz@example.com", "wrongPassword");

        when(userService.login(loginRequest.email(), loginRequest.password()))
            .thenThrow(new IllegalArgumentException("invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid email or password"));
    }
}
