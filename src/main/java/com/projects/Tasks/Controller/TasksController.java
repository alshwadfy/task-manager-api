package com.projects.Tasks.Controller;

import com.projects.Tasks.domain.dto.TaskDto;
import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.mappers.TaskMapper;
import com.projects.Tasks.services.TaskService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/task-lists/{task_list_id}/tasks")
public class TasksController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TasksController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public Page<TaskDto> getTaskList(@PathVariable("task_list_id") UUID task_list_id, Pageable pageable) {
        return taskService.listTasks(task_list_id, pageable)
                .map(taskMapper::toDto);
    }

    @PostMapping
    public TaskDto Createtask(@PathVariable("task_list_id") UUID task_list_id,
                              @RequestBody TaskDto taskDto) {
        Task  taskCreated= taskService.Createtask(task_list_id,
                taskMapper.fromDto(taskDto));
        return taskMapper.toDto(taskCreated);

    }
    @PutMapping(path = "{task_id}")
    public TaskDto UpdateTask(@PathVariable("task_list_id") UUID taskListId,
                              @PathVariable("task_id") UUID taskId,
                              @RequestBody TaskDto taskDto
                              ){
        Task updated =taskService.UpdateTask(taskListId,taskId,taskMapper.fromDto(taskDto));
        return taskMapper.toDto(updated);


    }
    @DeleteMapping(path = "{task_id}")
    public void DeleteTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId
    )
    {
        taskService.DeleteTaskWithListId(taskListId,taskId);
    }

     @GetMapping(path = "{task_id}")
    public ResponseEntity<TaskDto> GetTask(
            @PathVariable("task_list_id") UUID taskListId,
            @PathVariable("task_id") UUID taskId
    ) {
        return taskService.findTaskById(taskListId, taskId)
                .map(taskMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
