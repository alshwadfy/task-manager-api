package com.projects.Tasks.services;

import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {

    Page<TaskListDto> getTaskListsByUserId(UUID userId, Pageable pageable);
    User getUserById(UUID userId);
    User createUser(RegisterRequest registerRequest);
    User login(String email, String password);
    void deleteAllUser();
}
