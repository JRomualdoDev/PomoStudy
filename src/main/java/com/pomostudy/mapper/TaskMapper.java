package com.pomostudy.mapper;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
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
                task.getTimeTotalLearning(),
                task.getCategory().getId()
        );
    }

    public Task toCreateTask(TaskRequestDTO taskRequestDTO, AuthenticatedUser authenticatedUser) {

        Task task = new Task();

        Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getUser().getId());

        Optional<Category> category = categoryRepository.findByIdAndUser(taskRequestDTO.categoryId(), authenticatedUser.getUser());
        if (category.isEmpty())
            throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

        task.setName(taskRequestDTO.name());
        task.setDescription(taskRequestDTO.description());
        task.setStartDate(taskRequestDTO.startDate());
        task.setEndDate(taskRequestDTO.endDate());
        task.setStatus(taskRequestDTO.status());
        task.setPriority(taskRequestDTO.priority());
        task.setTimeTotalLearning(taskRequestDTO.timeTotalLearning());
        task.setUser(user.get());
        task.setCategory(category.get());

        return task;
    }

    public Task toUpdateTask(TaskRequestDTO taskRequestDTO, AuthenticatedUser authenticatedUser,Long id) {

        Task task = taskRepository.findById(id)
                    .filter(t -> t.getUser().getId().equals(authenticatedUser.getUser().getId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));

        Optional<Category> category = categoryRepository.findByIdAndUser(taskRequestDTO.categoryId(), authenticatedUser.getUser());
        if (category.isEmpty())
            throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

        task.setName(taskRequestDTO.name());
        task.setDescription(taskRequestDTO.description());
        task.setStartDate(taskRequestDTO.startDate());
        task.setEndDate(taskRequestDTO.endDate());
        task.setStatus(taskRequestDTO.status());
        task.setPriority(taskRequestDTO.priority());
        task.setTimeTotalLearning(taskRequestDTO.timeTotalLearning());
        task.setCategory(category.get());

        return task;
    }
}
