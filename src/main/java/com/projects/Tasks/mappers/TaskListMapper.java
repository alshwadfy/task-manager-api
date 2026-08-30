package com.projects.Tasks.mappers;

import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.TaskList;

public interface TaskListMapper {

    TaskList fromDto(TaskListDto taskListDto);
    TaskListDto toDto(TaskList taskList);


}
