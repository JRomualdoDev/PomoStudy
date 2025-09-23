package com.pomostudy.dto.task;

import com.pomostudy.entity.Category;
import com.pomostudy.entity.Task;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.TaskRepository;
import com.pomostudy.repository.UserRepository;
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
                task.getUserTask().getId(),
                task.getCategory().getId()
        );
    }

    public Task toCreateTask(TaskRequestDTO taskRequestDTO) {

        Task task = new Task();

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
        task.setUserTask(user.get());
        task.setCategory(category.get());

        // TODO: Campo pensar o que fazer
//        task.setCreatedAt(OffsetDateTime.now());

        return task;
    }

    public Task toUpdateTask(TaskRequestDTO taskRequestDTO, Long id) {

        Task task = taskRepository.findById(id)
                    .filter(t -> t.getUserTask().getId().equals(taskRequestDTO.user_task()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));

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
        task.setCategory(category.get());

        return task;
    }
}
