package com.pomoStudy.service;

import com.pomoStudy.dto.Task.TaskMapper;
import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.Task.TaskResponseDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.Task;
import com.pomoStudy.entity.User;
import com.pomoStudy.enums.StatusUser;
import com.pomoStudy.enums.TaskPriority;
import com.pomoStudy.exception.ResourceException;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.TaskRepository;
import com.pomoStudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;


    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;
    private Task task;
    private User user;
    private Category category;

    OffsetDateTime startDate = OffsetDateTime.now().plusDays(1L);
    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {

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
        task.setName("testTask");
        task.setDescription("loremipsumloremipsumloremipsum");
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setStatus(StatusUser.IN_PROGRESS);
        task.setPriority(TaskPriority.MEDIUM);
        task.setTimeTotalLearning(30);
        task.setUser_task(user);
        task.setCategory(category);

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
        taskResponseDTO = new TaskResponseDTO("testTask", "loremipsumloremipsumloremipsum", startDate, endDate, StatusUser.IN_PROGRESS, TaskPriority.MEDIUM, 30, 1L, 1L);
    }

    @Test
    @DisplayName("Should save Task with success in the db")
    void shouldSaveTaskSuccessfully() {

        when(taskMapper.toTask(any(TaskRequestDTO.class), eq(null))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(any(Task.class))).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.save(taskRequestDTO);

        assertNotNull(result);
        assertEquals("testTask", result.name());
        assertEquals("loremipsumloremipsumloremipsum", result.description());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(StatusUser.IN_PROGRESS, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());
        assertEquals(30, result.timeTotalLearning());
        assertEquals(1L, result.user_task());
        assertEquals(1L, result.categoryId());

        verify(taskMapper, times(1)).toTask(any(TaskRequestDTO.class), eq(null));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toTaskResponseDTO(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {

        when(taskMapper.toTask(any(TaskRequestDTO.class), eq(null))).thenThrow(ResourceExceptionFactory.notFound("User", taskRequestDTO.user_task()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.save(taskRequestDTO));
        assertEquals("User with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toTask(taskRequestDTO, null);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should edit task successfully in the db")
    void shouldEditTaskSuccessfully() {
        Long taskId = 1L;
        when(taskMapper.toTask(taskRequestDTO, taskId)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(any(Task.class))).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.edit(taskRequestDTO, taskId);

        assertNotNull(result);
        assertEquals("testTask", result.name());
        assertEquals("loremipsumloremipsumloremipsum", result.description());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(StatusUser.IN_PROGRESS, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());
        assertEquals(30, result.timeTotalLearning());
        assertEquals(1L, result.user_task());
        assertEquals(1L, result.categoryId());

        verify(taskMapper, times(1)).toTask(taskRequestDTO, taskId);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toTaskResponseDTO(task);
    }

    @Test
    @DisplayName("Should throw exception when user is not belong to task")
    void shouldThrowExceptionWhenUserNotBelongToTask() {

        Long taskId = 1L;
        when(taskMapper.toTask(taskRequestDTO, taskId)).thenThrow(ResourceExceptionFactory.notFound("Task", taskRequestDTO.user_task()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.edit(taskRequestDTO, taskId));

        assertEquals("Task with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toTask(taskRequestDTO, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }
}