package com.projects.Tasks.Controller;

import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.services.TaskListService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;

    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public Page<TaskListDto> getTaskLists(@RequestParam UUID userId, Pageable pageable) {
        return taskListService.findByUserId(userId, pageable)
                .map(taskListMapper::toDto);
    }

    @PostMapping
    public TaskListDto addTaskList(@RequestBody TaskListDto taskListDto, @RequestParam UUID userId) {
        TaskList created = taskListService.createTaskList(
                taskListMapper.fromDto(taskListDto),
                userId
        );
        return taskListMapper.toDto(created);
    }

    @GetMapping(path = "/{task_list_id}")
    public ResponseEntity<TaskListDto> getTaskListById(@PathVariable("task_list_id") UUID taskListId) {
        return taskListService.getTaskListById(taskListId)
                .map(taskListMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{task_list_id}")
    public TaskListDto updateTaskList(
            @PathVariable("task_list_id") UUID taskListId,
            @RequestBody TaskListDto taskListDto
    ) {
        TaskList updatedTaskList = taskListService.updateTaskList(
                taskListId,
                taskListMapper.fromDto(taskListDto)
        );
        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping(path = "/{task_list_id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable("task_list_id") UUID taskListId) {
        taskListService.deleteTaskList(taskListId);
        return ResponseEntity.noContent().build();
    }
}