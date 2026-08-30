package com.projects.Tasks.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.TaskListService;

@WebMvcTest(controllers = TaskListController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TaskListService taskListService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private TaskListMapper taskListMapper;

    @Test
    public void getTaskLists_ShouldReturn200AndList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        TaskList taskList = new TaskList(
            taskUuid,
            "Work",
            "Work tasks",
            null,
            null,
            null,
            null
        );
        TaskListDto taskListDto = new TaskListDto(
            taskList.getId(),
            "Work",
            "Work tasks",
            0,
            0.0,
            null,
            userId
        );

        when(taskListService.findByUserId(any(), any())).thenReturn(new PageImpl<>(List.of(taskList)));
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(
            get("/api/task-lists")
                .param("userId", userId.toString())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].title").value("Work"));
    }

    @Test
    public void getTaskLists_ShouldReturn200AndEmptyList_WhenEmpty() throws Exception {
        UUID userId = UUID.randomUUID();
        when(taskListService.findByUserId(any(), any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(
            get("/api/task-lists")
                .param("userId", userId.toString())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void getTaskListById_ShouldReturn200AndTaskList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        TaskList taskList = new TaskList(
            taskUuid,
            "Work",
            "Work tasks",
            null,
            null,
            null,
            null
        );
        TaskListDto taskListDto = new TaskListDto(
            taskList.getId(),
            "Work",
            "Work tasks",
            0,
            0.0,
            null,
            userId
        );

        when(taskListService.getTaskListById(taskUuid)).thenReturn(Optional.of(taskList));
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(
            get("/api/task-lists/" + taskUuid.toString())
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Work"));
    }

    @Test
    public void getTaskListById_ShouldReturn404_WhenNotFound() throws Exception {
        UUID taskUuid = UUID.randomUUID();
        when(taskListService.getTaskListById(taskUuid)).thenReturn(Optional.empty());

        mockMvc.perform(
            get("/api/task-lists/" + taskUuid.toString())
        )
        .andExpect(status().isNotFound());
    }

    @Test
    public void createTaskList_ShouldReturn200AndTaskList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        TaskList taskList = new TaskList(
            taskUuid,
            "Work",
            "Work tasks",
            null,
            null,
            null,
            null
        );
        TaskListDto taskListDto = new TaskListDto(
            taskList.getId(),
            "Work",
            "Work tasks",
            0,
            0.0,
            null,
            userId
        );

        when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
        when(taskListService.createTaskList(taskList, userId)).thenReturn(taskList);
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(
            post("/api/task-lists")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskListDto))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Work"));
    }

    @Test 
    public void deleteTaskList_ShouldReturn204NoContent() throws Exception {
        UUID taskUuid = UUID.randomUUID();

        doNothing().when(taskListService).deleteTaskList(taskUuid);

        mockMvc.perform(
            delete("/api/task-lists/" + taskUuid.toString())
        )
        .andExpect(status().isNoContent());
    }

    @Test
    public void updateTaskList_ShouldReturn200AndTaskList() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID taskUuid = UUID.randomUUID();
        TaskList taskList = new TaskList(
            taskUuid,
            "Work",
            "Work tasks",
            null,
            null,
            null,
            null
        );
        TaskListDto taskListDto = new TaskListDto(
            taskList.getId(),
            "Work",
            "Work tasks",
            0,
            0.0,
            null,
            userId
        );

        when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
        when(taskListService.updateTaskList(taskUuid, taskList)).thenReturn(taskList);
        when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

        mockMvc.perform(
            put("/api/task-lists/" + taskUuid.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskListDto))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Work"));
    }
}

