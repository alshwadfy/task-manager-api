package com.projects.Tasks.domain.dto;

import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        List <TaskListDto> taskLists
) {
}
