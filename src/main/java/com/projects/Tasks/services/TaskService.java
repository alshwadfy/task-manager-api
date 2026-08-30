package com.projects.Tasks.services;

import com.projects.Tasks.domain.entities.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService{

    Page<Task> listTasks(UUID listTaskId, Pageable pageable);
    Task Createtask(UUID taskListId,Task task);
    Optional<Task> findTaskById(UUID taskListId,UUID taskId);
    Task UpdateTask(UUID taskListId,UUID taskID,Task task);
    void DeleteTaskWithListId(UUID taskListId,UUID taskID);
}
