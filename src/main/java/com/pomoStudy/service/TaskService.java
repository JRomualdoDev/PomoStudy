package com.pomoStudy.service;

import com.pomoStudy.dto.Task.TaskMapper;
import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.Task.TaskResponseDTO;
import com.pomoStudy.entity.Task;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.TaskRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;
    final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository,
                       CategoryRepository categoryRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDTO save(TaskRequestDTO taskRequestDTO) {

        try {
            Task taskSave = taskRepository.save(taskMapper.toTask(taskRequestDTO));

            return taskMapper.toResponseDTO(taskSave);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error created Task.");
        }
    }

    public TaskResponseDTO edit(TaskRequestDTO taskRequestDTO, Long id) {


        Optional<Task> task = taskRepository.findById(id)
                .filter((t) -> t.getUser_task().getId().equals(taskRequestDTO.user_task()));
        if (task.isEmpty())
            throw ResourceExceptionFactory.notFound("Task", id);

        try {

            Task taskUpdate = taskRepository.save(taskMapper.toTask(taskRequestDTO));

            return taskMapper.toResponseDTO(taskUpdate);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updated task.");
        }
    }

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));
        return taskMapper.toResponseDTO(task);
    }

    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));
        taskRepository.deleteById(id);
    }
}
