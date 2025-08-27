package com.pomoStudy.service;

import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.category.CategoryResponseDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.Task;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.TaskRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class TaskService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public void save(TaskRequestDTO taskRequestDTO) {
        Optional<User> user = userRepository.findById(taskRequestDTO.user_task());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", taskRequestDTO.user_task());

        Optional<Category> category = categoryRepository.findById(taskRequestDTO.categoryId());
        if (category.isEmpty())
            throw ResourceExceptionFactory.notFound("Category", taskRequestDTO.categoryId());

        try {
            Task task =  new Task();
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

            taskRepository.save(task);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error created Task.");
        }
    }
}
