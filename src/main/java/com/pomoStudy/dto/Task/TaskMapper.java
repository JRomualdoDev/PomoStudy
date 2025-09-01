package com.pomoStudy.dto.Task;

import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.Task;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.TaskRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
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

    public TaskResponseDTO toResponseDTO(Task task) {
       return new TaskResponseDTO(
                task.getName(),
                task.getDescription(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getTimeTotalLearning(),
                task.getUser_task().getId(),
                task.getCategory().getId()
        );
    }

    public Task toTask(TaskRequestDTO taskRequestDTO, Long id) {

        Task task;

        if ( id != null) {
            task = taskRepository.findById(id)
                    .filter((t) -> t.getUser_task().getId().equals(taskRequestDTO.user_task()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));
        } else {
            task = new Task();
        }

        Optional<User> user = userRepository.findById(taskRequestDTO.user_task());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", taskRequestDTO.user_task());

        Optional<Category> category = categoryRepository.findById(taskRequestDTO.categoryId());
        if (category.isEmpty())
            throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

        task.setName(taskRequestDTO.name());
        task.setDescription(taskRequestDTO.description());
        task.setStartDate(taskRequestDTO.startDate());
        task.setEndDate(taskRequestDTO.endDate());
        task.setStatus(taskRequestDTO.status());
        task.setPriority(taskRequestDTO.priority());
        task.setTimeTotalLearning(taskRequestDTO.timeTotalLearning());
        task.setUser_task(user.get());
        task.setCategory(category.get());
        task.setCreatedAt(OffsetDateTime.now());

        return task;
    }
}
