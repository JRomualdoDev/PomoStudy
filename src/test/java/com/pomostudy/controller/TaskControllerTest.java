package com.pomostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import com.pomostudy.enums.StatusUser;
import com.pomostudy.enums.TaskPriority;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        category.setUserCategory(user);

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

    @Test
    @DisplayName("Should be return 404 Not Found for try edit non-existent task")
    void shouldReturn404TryEditNonExistentTask() throws Exception {

        when(taskService.edit(any(TaskRequestDTO.class), anyLong()))
                .thenThrow(ResourceExceptionFactory.notFound("Task", taskID));

        mockMvc.perform(put("/api/task/{id}", taskID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test

    @DisplayName("Should be return all Tasks with status 200 OK")

    void shouldReturnAllTasksWithStatus200() throws Exception {

        List<TaskResponseDTO> tasks = Arrays.asList(taskResponseDTO,
            new TaskResponseDTO(
                    2L,
                    "testTask2",
                    "loremipsumloremipsumloremipsum",
                    startDate,
                    endDate,
                    StatusUser.IN_PROGRESS,
                    TaskPriority.MEDIUM,
                    30,
                    1L,
                    1L));

        when(taskService.findAll(any(Pageable.class))).thenReturn(tasks);

        mockMvc.perform(get("/api/task?pageno=1&pagesize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("testTask"));

    }

    @Test
    @DisplayName("Should be found one task for the id and return 200 OK")
    void shouldFoundTaskForTheIdReturn200() throws Exception {

        when(taskService.findById(anyLong())).thenReturn(taskResponseDTO);

        mockMvc.perform(get("/api/task/{id}", taskID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testTask"))
                .andExpect(jsonPath("$.description").value("loremipsumloremipsumloremipsum"));
    }
}
