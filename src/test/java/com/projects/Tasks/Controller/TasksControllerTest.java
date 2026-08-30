package com.projects.Tasks.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.Tasks.domain.dto.TaskDto;
import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.TaskPriority;
import com.projects.Tasks.domain.entities.TaskStatus;
import com.projects.Tasks.mappers.TaskMapper;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.PageImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TasksController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void getTasks_ShouldReturn200AndList() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        TaskList taskList = new TaskList(taskListId, "Work", "Work tasks", null, null, null, null);
        Task task = new Task(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, taskList);
        TaskDto taskDto = new TaskDto(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM);

        when(taskService.listTasks(eq(taskListId), any(org.springframework.data.domain.Pageable.class))).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/api/task-lists/{task_list_id}/tasks", taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Task 1"));
    }

    @Test
    public void getTaskById_ShouldReturn200AndTask() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        TaskList taskList = new TaskList(taskListId, "Work", "Work tasks", null, null, null, null);
        Task task = new Task(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, taskList);
        TaskDto taskDto = new TaskDto(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM);

        when(taskService.findTaskById(taskListId, taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/api/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void getTaskById_ShouldReturn404_WhenNotFound() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        when(taskService.findTaskById(taskListId, taskId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTask_ShouldReturn200AndTaskDto() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        TaskList taskList = new TaskList(taskListId, "Work", "Work tasks", null, null, null, null);
        Task task = new Task(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, taskList);
        TaskDto taskDto = new TaskDto(taskId, "Task 1", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM);

        when(taskMapper.fromDto(any(TaskDto.class))).thenReturn(task);
        when(taskService.Createtask(eq(taskListId), any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(post("/api/task-lists/{task_list_id}/tasks", taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void updateTask_ShouldReturn200AndUpdatedTaskDto() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        TaskList taskList = new TaskList(taskListId, "Work", "Work tasks", null, null, null, null);
        Task task = new Task(taskId, "Task 1 Updated", "Description", null, TaskStatus.CLOSED, TaskPriority.HIGH, null, null, taskList);
        TaskDto taskDto = new TaskDto(taskId, "Task 1 Updated", "Description", null, TaskStatus.CLOSED, TaskPriority.HIGH);

        when(taskMapper.fromDto(any(TaskDto.class))).thenReturn(task);
        when(taskService.UpdateTask(eq(taskListId), eq(taskId), any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        mockMvc.perform(put("/api/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1 Updated"));
    }

    @Test
    public void deleteTask_ShouldReturn200() throws Exception {
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        doNothing().when(taskService).DeleteTaskWithListId(taskListId, taskId);

        mockMvc.perform(delete("/api/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk());

        verify(taskService, times(1)).DeleteTaskWithListId(taskListId, taskId);
    }
}
