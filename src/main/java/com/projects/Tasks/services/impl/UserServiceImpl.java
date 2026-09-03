package com.projects.Tasks.services.impl;

import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.domain.dto.TaskListDto;

import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.repositories.TaskListRepo;
import com.projects.Tasks.repositories.UserRepo;

import com.projects.Tasks.exceptions.ResourceNotFoundException;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final TaskListRepo taskListRepo;
    private final UserRepo userRepo;
    private final TaskListMapper taskListMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(TaskListRepo taskListRepo, UserRepo userRepo, TaskListMapper taskListMapper, PasswordEncoder passwordEncoder, JwtService jwtService) {

        this.taskListRepo = taskListRepo;
        this.userRepo = userRepo;
        this.taskListMapper = taskListMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        
    }


    @Override
    public Page<TaskListDto> getTaskListsByUserId(UUID userId, Pageable pageable) {
        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }
        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        return taskListRepo.findByUserUserId(userId, pageable)
                .map(taskListMapper::toDto);
    }

    @Override
    public User getUserById(UUID userId) {
        if(userId==null){
            throw new IllegalArgumentException("userId is null");

        }
        return userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("userId "+userId+" not found"));


    }




    @Override
    public User createUser(RegisterRequest registerRequest) {
        if (null == registerRequest.username() || registerRequest.username().isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (null == registerRequest.email() || registerRequest.email().isEmpty()) {
            throw new IllegalArgumentException("email is required");
        }
        if (null == registerRequest.password() || registerRequest.password().isEmpty()) {
            throw new IllegalArgumentException("password is required");
        }
        if (userRepo.existsByEmail(registerRequest.email())) {
            throw new IllegalArgumentException("email already registered");
        }
        if(userRepo.existsByUsername(registerRequest.username())){
            throw new IllegalArgumentException("username already registered");
        }
        if () {
            
        }
        String hashed = passwordEncoder.encode(registerRequest.password());
        User user = new User(null, registerRequest.username(), registerRequest.email(), hashed, null);
        return userRepo.save(user);
    }


    @Override
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("invalid email or password");
        }
        return user;
    }

    @Override
    public void deleteAllUser() {
        userRepo.deleteAll();
    }
}
