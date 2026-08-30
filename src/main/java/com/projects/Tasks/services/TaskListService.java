package com.projects.Tasks.services;

import com.projects.Tasks.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskListService {
    List<TaskList> listTaskList();
    TaskList createTaskList(TaskList taskList,UUID userId);
    Optional<TaskList> getTaskListById(UUID id);
    TaskList updateTaskList(UUID taskListid, TaskList taskList);
    void deleteTaskList(UUID id);
    Page<TaskList> findByUserId(UUID userId, Pageable pageable);
}
