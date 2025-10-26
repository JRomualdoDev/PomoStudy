package com.pomostudy.mapper;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.task.TaskRequestCreateDTO;
import com.pomostudy.dto.task.TaskRequestUpdateDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.dto.task.TaskResponseMonthDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.TaskRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskMapper {

    UserRepository userRepository;
    CategoryRepository categoryRepository;
    TaskRepository taskRepository;

    public TaskMapper(UserRepository userRepository, CategoryRepository categoryRepository,
            TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    public TaskResponseDTO toTaskResponseDTO(Task task) {
       return new TaskResponseDTO(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getTimeTotalLearning()
        );
    }

    public TaskResponseMonthDTO toTaskResponseMonthDTO(Task task) {
       return new TaskResponseMonthDTO(
                task.getId(),
                task.getName(),
                task.getStartDate()
        );
    }

    public Task toCreateTask(TaskRequestCreateDTO taskRequestDTO, AuthenticatedUser authenticatedUser) {

        Task task = new Task();

        Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getUser().getId());

        if ( taskRequestDTO.categoryId() != null) {
            Optional<Category> category = categoryRepository.findByIdAndUser(taskRequestDTO.categoryId(), authenticatedUser.getUser());

            if (category.isEmpty())
                throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

            task.setCategory(category.get());

        }

        task.setName(taskRequestDTO.name());
        task.setDescription(taskRequestDTO.description());
        task.setStartDate(taskRequestDTO.startDate());
        task.setEndDate(taskRequestDTO.endDate());
        task.setStatus(taskRequestDTO.status());
        task.setPriority(taskRequestDTO.priority());
        task.setTimeTotalLearning(taskRequestDTO.timeTotalLearning());
        task.setUser(user.get());

        return task;
    }

    public Task toUpdateTask(TaskRequestUpdateDTO taskRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

        Task task = taskRepository.findById(id)
                    .filter(t -> t.getUser().getId().equals(authenticatedUser.getUser().getId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));

        if ( taskRequestDTO.categoryId() != null) {
            Optional<Category> category = categoryRepository.findByIdAndUser(taskRequestDTO.categoryId(), authenticatedUser.getUser());

            if (category.isEmpty())
                throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

            task.setCategory(category.get());

        }

        Optional.ofNullable(taskRequestDTO.name()).ifPresent(task::setName);
        Optional.ofNullable(taskRequestDTO.description()).ifPresent(task::setDescription);
        Optional.ofNullable(taskRequestDTO.startDate()).ifPresent(task::setStartDate);
        Optional.ofNullable(taskRequestDTO.endDate()).ifPresent(task::setEndDate);
        Optional.ofNullable(taskRequestDTO.status()).ifPresent(task::setStatus);
        Optional.ofNullable(taskRequestDTO.priority()).ifPresent(task::setPriority);
        Optional.ofNullable(taskRequestDTO.timeTotalLearning()).ifPresent(task::setTimeTotalLearning);

        return task;
    }
}
