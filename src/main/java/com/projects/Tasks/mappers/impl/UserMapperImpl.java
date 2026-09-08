package com.projects.Tasks.mappers.impl;

import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserMapperImpl implements UserMapper {
private final TaskListImpl taskListImpl;

    public UserMapperImpl(TaskListImpl taskListImpl) {
        this.taskListImpl = taskListImpl;
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getVerified(),
                Optional.ofNullable(user.getTaskLists())
                        .map(tasks -> tasks.stream().map(taskListImpl::toDto).toList())
                        .orElse(List.of())
        );
    }


}
