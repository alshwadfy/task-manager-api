package com.projects.Tasks.mappers.impl;

import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.TaskStatus;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.mappers.TaskMapper;
import com.projects.Tasks.repositories.UserRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class TaskListImpl implements TaskListMapper {

    private final TaskMapper taskMapper;
    private final UserRepo userRepo;

    public TaskListImpl(TaskMapper taskMapper, UserRepo userRepo) {
        this.taskMapper = taskMapper;
        this.userRepo = userRepo;
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        User user = Optional.ofNullable(taskListDto.userId())
                .flatMap(userRepo::findById)
                .orElse(null);

        return new TaskList(
                taskListDto.id(),
                taskListDto.title(),
                taskListDto.description(),
                Optional.ofNullable(taskListDto.tasks())
                        .map(tasks -> tasks.stream().map(taskMapper::fromDto).toList())
                        .orElse(null),
                null,
                null,
                user
        );
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks()).map(List::size).orElse(0),
                calculateTaskListProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks())
                        .map(tasks -> tasks.stream().map(taskMapper::toDto).toList())
                        .orElse(null),
                Optional.ofNullable(taskList.getUser())
                        .map(User::getUserId)
                        .orElse(null)
        );
    }

    private Double calculateTaskListProgress(List<Task> taskList) {
        if (null == taskList) {
            return null;
        }
        long closedTaskCount = taskList.stream()
                .filter(task -> TaskStatus.CLOSED == task.getStatus())
                .count();
        return (double) closedTaskCount / taskList.size();
    }
}