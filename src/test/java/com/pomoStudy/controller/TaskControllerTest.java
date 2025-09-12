package com.pomoStudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.Task.TaskResponseDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.Task;
import com.pomoStudy.entity.User;
import com.pomoStudy.enums.StatusUser;
import com.pomoStudy.enums.TaskPriority;
import com.pomoStudy.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;
    private Task task;
    private User user;
    private Category category;
    private final Long taskID = 1L;

    OffsetDateTime startDate = OffsetDateTime.now().plusDays(1L);
    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {
        taskRequestDTO = new TaskRequestDTO(
                "testTask",
                "loremipsumloremipsumloremipsum",
                startDate,
                endDate,
                StatusUser.IN_PROGRESS,
                TaskPriority.MEDIUM,
                30,
                1L,
                1L);
        taskResponseDTO = new TaskResponseDTO(
                1L,
                "testTask",
                "loremipsumloremipsumloremipsum",
                startDate,
                endDate,
                StatusUser.IN_PROGRESS,
                TaskPriority.MEDIUM,
                30,
                1L,
                1L);

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        category = new Category();
        category.setName("categoryTest");
        category.setColor("#FFF");
        category.setIcon("test.icon");
        category.setUser_category(user);

        task = new Task();
        task.setId(1L);
        task.setName("testTask");
        task.setDescription("loremipsumloremipsumloremipsum");
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setStatus(StatusUser.IN_PROGRESS);
        task.setPriority(TaskPriority.MEDIUM);
        task.setTimeTotalLearning(30);
        task.setUser_task(user);
        task.setCategory(category);
    }

    @Test
    @DisplayName("Should be create task and return status 201 created")
    void shouldCreateTaskReturnStatus201() throws Exception {

        when(taskService.save(any(TaskRequestDTO.class))).thenReturn(taskResponseDTO);

        mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("api/task/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("testTask")));
    }

    @Test
    @DisplayName("Should be return 400 Bad request when create a new task with invalid data")
    void shouldBeReturn400BadRequestWhenCreateNewTaskWithInvalidData() throws Exception {

        TaskRequestDTO invalidTaskRequestDTO = new TaskRequestDTO(
                null,
                "loremipsumloremipsumloremipsum",
                startDate,
                endDate,
                StatusUser.IN_PROGRESS,
                TaskPriority.MEDIUM,
                30,
                1L,
                1L);

        mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTaskRequestDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should be edit task and return status 200 ok")
    void shouldEditTaskAndReturnStatus200OK() throws Exception {

        when(taskService.edit(any(TaskRequestDTO.class), anyLong())).thenReturn(taskResponseDTO);

        mockMvc.perform(put("/api/task/{id}", taskID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("testTask"));

    }

}
