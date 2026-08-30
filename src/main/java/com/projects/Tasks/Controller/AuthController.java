package com.projects.Tasks.Controller;

import com.projects.Tasks.domain.dto.AuthResponse;
import com.projects.Tasks.domain.dto.LoginRequest;
import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.UserMapper;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public AuthController(UserService userService, UserMapper userMapper, JwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest registerRequest) {
        User created = userService.createUser(registerRequest);
        return userMapper.toDto(created);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.email(), loginRequest.password());
        String token = jwtService.generateToken(user.getUserId());
        return new AuthResponse(token, user.getUserId(), user.getUsername(), user.getEmail());
    }
}