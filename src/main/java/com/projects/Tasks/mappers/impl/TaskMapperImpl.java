package com.projects.Tasks.mappers.impl;

import com.projects.Tasks.domain.dto.TaskDto;
import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.mappers.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task fromDto(TaskDto dto) {
      return new Task(
              dto.id(),
              dto.title(),
              dto.description(),
              dto.dueDate(),
              dto.status(),
              dto.priority(),
              null,
            null,
         null
      );

    }

    @Override
    public TaskDto toDto(Task task) {
return new TaskDto(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getDueDate(),
        task.getStatus(),
        task.getPriority()
);

    }
}
