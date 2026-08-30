package com.projects.Tasks.services.impl;

import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.TaskPriority;
import com.projects.Tasks.domain.entities.TaskStatus;
import com.projects.Tasks.repositories.TaskListRepo;
import com.projects.Tasks.repositories.TaskRepo;
import com.projects.Tasks.services.TaskListService;
import com.projects.Tasks.services.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projects.Tasks.exceptions.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final TaskListRepo taskListRepo;

    public TaskServiceImpl(TaskRepo taskRepo, TaskListRepo taskListRepo) {
        this.taskRepo = taskRepo;
        this.taskListRepo = taskListRepo;
    }
    @Override
    public Page<Task> listTasks(UUID listTaskId ,Pageable pageable) {
        return taskRepo.findByTaskListId(listTaskId,pageable);
    }

    @Override
    public Task Createtask(UUID taskListId, Task task) {
        if (task.getId()!= null) {
            throw new IllegalArgumentException("Task id already exists");
        }
        if ( null ==task.getTitle()||task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task title required");
        }
        TaskPriority priority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.LOW);
        TaskStatus status = TaskStatus.OPEN;

      TaskList taskList= taskListRepo.findById(taskListId)
                .orElseThrow(() -> new ResourceNotFoundException("Task id not found"));
                Task newTask = new Task(
                            null,
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        status,
                        priority,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        taskList




                );
                return taskRepo.save(newTask);

    }

    @Override
    public Optional<Task> findTaskById(UUID taskList,UUID taskId) {
        return taskRepo.findByTaskListIdAndId(taskList,taskId);
    }

    @Transactional
    @Override
    public Task UpdateTask(UUID taskListId,UUID taskID,Task task) {
        taskListRepo.findById(taskListId).orElseThrow(() -> new ResourceNotFoundException("Task id not found"));
        Task oldTask=taskRepo.findById(taskID).orElseThrow(() -> new ResourceNotFoundException("Task not found"));


        if(null == task.getDueDate()) {
            throw new IllegalArgumentException("Task due date required");
        }
        if(null == task.getStatus()) {
            throw new IllegalArgumentException("Task status required");
        }
        if(null == task.getTitle()) {
            throw new IllegalArgumentException("Task title required");
        }
        if(null == task.getDescription()) {
            throw new IllegalArgumentException("Task description required");
        }
        LocalDateTime now = LocalDateTime.now();
        oldTask.setUpdated(now);
        oldTask.setTitle(task.getTitle());
        oldTask.setDescription(task.getDescription());
        oldTask.setPriority(task.getPriority());
        oldTask.setStatus(task.getStatus());
        oldTask.setDueDate(task.getDueDate());



        return taskRepo.save(oldTask);
    }
    @Transactional
    @Override
    public void DeleteTaskWithListId(UUID taskListId, UUID taskID) {
        if (null ==taskID||taskListId==null) {
            throw new IllegalArgumentException("Task id required");
        }
        taskRepo.deleteByTaskListIdAndId(taskListId,taskID);



    }
}
