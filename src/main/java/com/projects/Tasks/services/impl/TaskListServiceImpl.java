package com.projects.Tasks.services.impl;

import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.repositories.TaskListRepo;
import com.projects.Tasks.services.TaskListService;
import com.projects.Tasks.services.UserService;
import org.springframework.stereotype.Service;
import com.projects.Tasks.exceptions.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepo taskListRepo;
    private final UserService userService;

    public TaskListServiceImpl(TaskListRepo taskListRepo, UserService userService) {
        this.taskListRepo = taskListRepo;
        this.userService = userService;
    }

    @Override
    public List<TaskList> listTaskList() {
        return taskListRepo.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList, UUID userId) {
        if (null != taskList.getId()) {
            throw new IllegalArgumentException("TaskList id already exists");
        }
        if (null == taskList.getTitle() || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("TaskList title MUST exist");
        }
        if (null == userId) {
            throw new IllegalArgumentException("userId is required");
        }
        if (taskListRepo.existsByUserUserIdAndTitle(userId, taskList.getTitle())) {
            throw new IllegalArgumentException("You already have a task list with this title");
        }
        LocalDateTime now = LocalDateTime.now();
        return taskListRepo.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now,
                userService.getUserById(userId)
        ));
    }

    @Override
    public Optional<TaskList> getTaskListById(UUID id) {
        return taskListRepo.findById(id);
    }

    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        TaskList oldTaskList = taskListRepo.findById(taskListId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskList id not found"));

        if (null == taskList.getTitle() || taskList.getTitle().isBlank()) {
            throw new IllegalArgumentException("TaskList title MUST exist");
        }
        if (null == taskList.getDescription() || taskList.getDescription().isBlank()) {
            throw new IllegalArgumentException("TaskList description MUST exist");
        }
        oldTaskList.setTitle(taskList.getTitle());
        oldTaskList.setDescription(taskList.getDescription());
        oldTaskList.setUpdated(LocalDateTime.now());
        return taskListRepo.save(oldTaskList);
    }

    @Override
    public void deleteTaskList(UUID id) {
        taskListRepo.deleteById(id);
    }

    @Override
    public Page<TaskList> findByUserId(UUID userId, Pageable pageable) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return taskListRepo.findByUserUserId(userId, pageable);
    }
}