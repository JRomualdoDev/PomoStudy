package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.task.TaskRequestUpdateDTO;
import com.pomostudy.dto.task.TaskResponseMonthDTO;
import com.pomostudy.mapper.TaskMapper;
import com.pomostudy.dto.task.TaskRequestCreateDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.entity.Task;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.TaskRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;
    private final AuthorizationService authorizationService;

    private static final String TASK = "Task";

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TaskMapper taskMapper,
            AuthorizationService authorizationService
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.taskMapper = taskMapper;
        this.authorizationService = authorizationService;
    }

    public TaskResponseDTO save(TaskRequestCreateDTO taskRequestDTO, AuthenticatedUser authenticatedUser) {

        Task taskSave = taskRepository.save(taskMapper.toCreateTask(taskRequestDTO, authenticatedUser));

        return taskMapper.toTaskResponseDTO(taskSave);
    }

    public TaskResponseDTO edit(TaskRequestUpdateDTO taskRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Task.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(TASK, id);
        }

        Task taskUpdate = taskRepository.save(taskMapper.toUpdateTask(taskRequestDTO, authenticatedUser,id));

        return taskMapper.toTaskResponseDTO(taskUpdate);
    }

    public Page<TaskResponseDTO> findAll(Pageable pageable, AuthenticatedUser authenticatedUser, Long categoryId) {

        Page<Task> taskPage;

        if (authenticatedUser.isAdmin()) {
            if (categoryId != null) {
                taskPage = taskRepository.findByCategoryId(categoryId, pageable);
            } else {
                taskPage = taskRepository.findAll(pageable);
            }
        } else {
            if (categoryId != null) {
                taskPage = taskRepository.findByCategoryId(categoryId, pageable);
            } else {
                taskPage = taskRepository.findByUser(authenticatedUser.getUser(), pageable);
            }
        }

        return taskPage.map(taskMapper::toTaskResponseDTO);
    }

    public Page<TaskResponseMonthDTO> findAllTaskByMonth(Pageable pageable, AuthenticatedUser authenticatedUser, String month) {

        Long userId = authenticatedUser.getUser().getId();

        YearMonth currentMonth = YearMonth.parse(month);

        ZoneOffset offset = ZoneOffset.UTC;

        OffsetDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay().atOffset(offset);
        OffsetDateTime startOfNextMonth = currentMonth.plusMonths(1).atDay(1).atStartOfDay().atOffset(offset);

        Page<Task> taskPage = taskRepository.findAllTaskByMonth(userId, startOfMonth, startOfNextMonth, pageable);

        return taskPage.map(taskMapper::toTaskResponseMonthDTO);
    }

    public TaskResponseDTO findById(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Task.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(TASK, id);
        }

        return taskRepository.findById(id)
                .map(TaskResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(TASK, id));
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Task.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(TASK, id);
        }

        taskRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(TASK, id));
        taskRepository.deleteById(id);
    }
}
