package com.projects.Tasks.mappers;

import com.projects.Tasks.domain.dto.TaskDto;
import com.projects.Tasks.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto dto);

    TaskDto toDto(Task task);
}
