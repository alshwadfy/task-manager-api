package com.projects.Tasks.domain.dto;

import com.projects.Tasks.domain.entities.TaskPriority;
import com.projects.Tasks.domain.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskStatus status,
        TaskPriority priority
) {
}
