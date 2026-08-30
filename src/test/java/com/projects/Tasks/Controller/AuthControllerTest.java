package com.projects.Tasks.Controller;

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
import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.UserMapper;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.UserService;

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

    @Test
    public void login_ShouldReturn200AndAuthResponse() throws Exception {
        LoginRequest loginRequest = new LoginRequest("ezz@example.com", "password");
        UUID userId = UUID.randomUUID();
        User user = new User(
            userId,
            "ezz",
            "ezz@example.com",
            "Hashedpassword",
            null
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
        User user = new User(userId, "ezz", "ezz@example.com", "Hashedpassword", null);
        UserDto userDto = new UserDto(userId, "ezz", "ezz@example.com", null);

        when(userService.createUser(any(RegisterRequest.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ezz"))
                .andExpect(jsonPath("$.email").value("ezz@example.com"));
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
