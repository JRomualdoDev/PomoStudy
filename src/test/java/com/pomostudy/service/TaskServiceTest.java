package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.task.TaskRequestUpdateDTO;
import com.pomostudy.dto.task.TaskResponseMonthDTO;
import com.pomostudy.enums.StatusTask;
import com.pomostudy.mapper.TaskMapper;
import com.pomostudy.dto.task.TaskRequestCreateDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
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
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private TaskService taskService;

    private User user;

    private TaskRequestCreateDTO taskRequestCreateDTO;
    private TaskRequestUpdateDTO taskRequestUpdateDTO;
    private TaskResponseDTO taskResponseDTO;
    private Task task;

    OffsetDateTime startDate = OffsetDateTime.now().plusDays(1L);
    OffsetDateTime endDate = OffsetDateTime.now().plusDays(4L);

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        Category category = new Category();
        category.setId(1L);
        category.setName("categoryTest");
        category.setColor("#FFF");
        category.setIcon("test.icon");
        category.setUser(user);

        task = new Task();
        task.setId(1L);
        task.setName("testTask");
        task.setDescription("loremipsumloremipsumloremipsum");
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setStatus(StatusTask.IN_PROGRESS);
        task.setPriority(TaskPriority.MEDIUM);
        task.setTimeTotalLearning(30);
        task.setUser(user);
        task.setCategory(category);

        taskRequestCreateDTO = new TaskRequestCreateDTO(
                "testTask",
                "loremipsumloremipsumloremipsum",
                startDate,
                endDate,
                StatusTask.IN_PROGRESS,
                TaskPriority.MEDIUM,
                30,
                1L
                );

        taskRequestUpdateDTO = new TaskRequestUpdateDTO(
                "testTask",
                "loremipsumloremipsumloremipsum",
                startDate,
                endDate,
                StatusTask.IN_PROGRESS,
                TaskPriority.MEDIUM,
                30,
                1L
                );
        taskResponseDTO = new TaskResponseDTO(1L, "testTask", "loremipsumloremipsumloremipsum", startDate, endDate, StatusTask.IN_PROGRESS, TaskPriority.MEDIUM, 30);
    }

    @Test
    @DisplayName("Should save Task with success in the db")
    void shouldSaveTaskSuccessfully() {

        when(taskMapper.toCreateTask(any(TaskRequestCreateDTO.class), any(AuthenticatedUser.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(any(Task.class))).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.save(taskRequestCreateDTO, authenticatedUser);

        assertNotNull(result);
        assertEquals("testTask", result.name());
        assertEquals("loremipsumloremipsumloremipsum", result.description());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(StatusTask.IN_PROGRESS, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());
        assertEquals(30, result.timeTotalLearning());

        verify(taskMapper, times(1)).toCreateTask(any(TaskRequestCreateDTO.class), any(AuthenticatedUser.class));
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toTaskResponseDTO(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {

        when(taskMapper.toCreateTask(any(TaskRequestCreateDTO.class), any(AuthenticatedUser.class))).thenThrow(ResourceExceptionFactory.notFound("User", user.getId()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.save(taskRequestCreateDTO, authenticatedUser));
        assertEquals("User with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toCreateTask(taskRequestCreateDTO, authenticatedUser);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should edit task successfully in the db")
    void shouldEditTaskSuccessfully() {
        Long taskId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskMapper.toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId)).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(any(Task.class))).thenReturn(taskResponseDTO);

        TaskResponseDTO result = taskService.edit(taskRequestUpdateDTO, authenticatedUser, taskId);

        assertNotNull(result);
        assertEquals("testTask", result.name());
        assertEquals("loremipsumloremipsumloremipsum", result.description());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
        assertEquals(StatusTask.IN_PROGRESS, result.status());
        assertEquals(TaskPriority.MEDIUM, result.priority());
        assertEquals(30, result.timeTotalLearning());

        verify(taskMapper, times(1)).toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).toTaskResponseDTO(task);
    }

    @Test
    @DisplayName("Should throw exception when user is not belong to task")
    void shouldThrowExceptionWhenUserNotBelongToTask() {

        Long taskId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskMapper.toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId)).thenThrow(ResourceExceptionFactory.notFound("Task", user.getId()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.edit(taskRequestUpdateDTO, authenticatedUser, taskId));

        assertEquals("Task with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when category does not found")
    void shouldThrowExceptionWhenCategoryNotFound() {

        Long taskId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskMapper.toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId)).thenThrow(ResourceExceptionFactory.notFound("Category", taskRequestUpdateDTO.categoryId()));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.edit(taskRequestUpdateDTO, authenticatedUser, taskId));

        assertEquals("Category with id 1 not found.", error.getMessage());

        verify(taskMapper, times(1)).toUpdateTask(taskRequestUpdateDTO, authenticatedUser, taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should find all tasks for an ADMIN user and return 200 OK")
    void shouldFindAllTasksForAdmin() {

        List<Task> taskResponse = Collections.singletonList(task);
        Pageable pageable = PageRequest.of(0, 10);


        when(authenticatedUser.isAdmin()).thenReturn(true);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(taskResponse));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);


        Page<TaskResponseDTO> result = taskService.findAll(pageable, authenticatedUser, null);


        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        verify(taskRepository, times(1)).findAll(any(Pageable.class));
        verify(taskRepository, never()).findByUser(any(), any());
    }

    @Test
    @DisplayName("Should find tasks for a NON-ADMIN user and return 200 OK")
    void shouldFindAllTasksDBWithStatus200() {

        List<Task> taskResponse = Collections.singletonList(task);
        Pageable pageable = PageRequest.of(0,10);

        when(authenticatedUser.isAdmin()).thenReturn(false);
        when(authenticatedUser.getUser()).thenReturn(user);

        when(taskRepository.findByUser(user, pageable)).thenReturn(new PageImpl<>(taskResponse));
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

        Page<TaskResponseDTO> result = taskService.findAll(pageable, authenticatedUser, null);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(taskResponseDTO,result.getContent().getFirst());
        assertEquals(taskRequestCreateDTO.name(), result.getContent().getFirst().name());

        verify(taskRepository, never()).findAll(pageable);
        verify(taskRepository, times(1)).findByUser(user, pageable);
    }

    @Test
    @DisplayName("Should find all tasks Per Month")
    void shouldFindAllTasksPerMonth() {

        String month = "2025-10";
        Pageable pageable = PageRequest.of(0, 10);
        Long userId = 1L;
        YearMonth currentMonth = YearMonth.parse(month);
        ZoneOffset offset = ZoneOffset.UTC;
        OffsetDateTime expectedStartOfMonth = currentMonth.atDay(1).atStartOfDay().atOffset(offset);
        OffsetDateTime expectedStartOfNextMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay().atOffset(offset);


        when(authenticatedUser.getUser()).thenReturn(user);

        Task task1 = new Task();
        task1.setId(100L);
        Task task2 = new Task();
        task2.setId(101L);
        List<Task> taskList = List.of(task1, task2);

        Page<Task> taskPage = new PageImpl<>(taskList, pageable, taskList.size());

        TaskResponseMonthDTO dto1 = new TaskResponseMonthDTO(task1);
        TaskResponseMonthDTO dto2 = new TaskResponseMonthDTO(task2);

        when(taskRepository.findAllTaskByMonth(
                user.getId(),
                expectedStartOfMonth,
                expectedStartOfNextMonth,
                pageable
        )).thenReturn(taskPage);

        when(taskMapper.toTaskResponseMonthDTO(task1)).thenReturn(dto1);
        when(taskMapper.toTaskResponseMonthDTO(task2)).thenReturn(dto2);

        Page<TaskResponseMonthDTO> result = taskService.findAllTaskByMonth(pageable, authenticatedUser, month);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
        assertEquals(dto1.id(), result.getContent().get(0).id());
        assertEquals(dto2.id(), result.getContent().get(1).id());

        verify(taskRepository, times(1)).findAllTaskByMonth(
                user.getId(),
                expectedStartOfMonth,
                expectedStartOfNextMonth,
                pageable
        );

        verify(taskMapper, times(1)).toTaskResponseMonthDTO(task1);
        verify(taskMapper, times(1)).toTaskResponseMonthDTO(task2);

    }

    @Test
    @DisplayName("Should find the task from id when the task exist in the db")
    void shouldFindUserFromIdWhenUserExistDB() {
        Long taskId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<TaskResponseDTO> result = Optional.ofNullable(taskService.findById(taskId, authenticatedUser));

        assertTrue(result.isPresent());
        assertEquals(taskResponseDTO, result.get());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should get Exception when do not find task from id")
    void shouldGetExceptionWhenNotFindTaskFromId() {
        Long taskId = 99L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskRepository.findById(taskId)).thenThrow(ResourceExceptionFactory.notFound("Task", taskId));

        ResourceException error = assertThrows(ResourceException.class, () -> taskService.findById(taskId, authenticatedUser));

        assertEquals("Task with id 99 not found.", error.getMessage());

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should delete task succefully from db")
    void shouldDeleteTaskSuccessfullyDB() {
        Long taskId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(authorizationService.isOwner(Task.class, taskId, user.getId())).thenReturn(true);
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(task));
        doNothing().when(taskRepository).deleteById(taskId);

        taskService.delete(taskId, authenticatedUser);

        verify(taskRepository, times(1)).deleteById(taskId);
    }
}