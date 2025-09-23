package com.pomostudy.service;

import com.pomostudy.dto.task.TaskMapper;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import com.pomostudy.enums.StatusUser;
import com.pomostudy.enums.TaskPriority;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.TaskRepository;
import com.pomostudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
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

    OffsetDateTime startDate = OffsetDateTime.now().plusDays(1L);
    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        Category category = new Category();
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
        task.setUserTask(user);
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
        taskResponseDTO = new TaskResponseDTO(1L, "testTask", "loremipsumloremipsumloremipsum", startDate, endDate, StatusUser.IN_PROGRESS, TaskPriority.MEDIUM, 30, 1L, 1L);
    }

    @Test
    @DisplayName("Should save Task with success in the db")
    void shouldSaveTaskSuccessfully() {

        when(taskMapper.toCreateTask(any(TaskRequestDTO.class))).thenReturn(task);
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

        verify(taskMapper, times(1)).toCreateTask(any(TaskRequestDTO.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toTaskResponseDTO(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {

        when(taskMapper.toCreateTask(any(TaskRequestDTO.class))).thenThrow(ResourceExceptionFactory.notFound("User", taskRequestDTO.user_task()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.save(taskRequestDTO));
        assertEquals("User with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toCreateTask(taskRequestDTO);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should edit task successfully in the db")
    void shouldEditTaskSuccessfully() {
        Long taskId = 1L;
        when(taskMapper.toUpdateTask(taskRequestDTO, taskId)).thenReturn(task);
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

        verify(taskMapper, times(1)).toUpdateTask(taskRequestDTO, taskId);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toTaskResponseDTO(task);
    }

    @Test
    @DisplayName("Should throw exception when user is not belong to task")
    void shouldThrowExceptionWhenUserNotBelongToTask() {

        Long taskId = 1L;
        when(taskMapper.toUpdateTask(taskRequestDTO, taskId)).thenThrow(ResourceExceptionFactory.notFound("Task", taskRequestDTO.user_task()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.edit(taskRequestDTO, taskId));

        assertEquals("Task with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toUpdateTask(taskRequestDTO, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when category does not found")
    void shouldThrowExceptionWhenCategoryNotFound() {

        Long taskId = 1L;
        when(taskMapper.toUpdateTask(taskRequestDTO, taskId)).thenThrow(ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.edit(taskRequestDTO, taskId));

        assertEquals("Category with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toUpdateTask(taskRequestDTO, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should be pass correct pagination parameters to service and return 200 OK")
    void shouldFindAllTasksDBWithStatus200() {

        List<Task> taskResponse = Collections.singletonList(task);
        Pageable pageable = PageRequest.of(0,10);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(taskResponse));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

        Page<TaskResponseDTO> result = taskService.findAll(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(taskResponseDTO,result.getContent().getFirst());
        assertEquals(taskRequestDTO.name(), result.getContent().getFirst().name());

        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find the task from id when the task exist in the db")
    void shouldFindUserFromIdWhenUserExistDB() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

        Optional<TaskResponseDTO> result = Optional.ofNullable(taskService.findById(taskId));

        assertTrue(result.isPresent());
        assertEquals(taskResponseDTO, result.get());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should get Exception when do not find task from id")
    void shouldGetExceptionWhenNotFindTaskFromId() {
        Long taskId = 99L;
        when(taskRepository.findById(taskId)).thenThrow(ResourceExceptionFactory.notFound("Task", taskId));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.findById(taskId));

        assertEquals("Task with id 99 not found.", error.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should delete task succefully from db")
    void shouldDeleteTaskSuccessfullyDB() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.delete(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }
}