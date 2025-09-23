package com.pomostudy.service;

import com.pomostudy.dto.task.TaskMapper;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.entity.Task;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.TaskRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        Task taskSave = taskRepository.save(taskMapper.toCreateTask(taskRequestDTO));

        return taskMapper.toTaskResponseDTO(taskSave);
    }

    public TaskResponseDTO edit(TaskRequestDTO taskRequestDTO, Long id) {

            Task taskUpdate = taskRepository.save(taskMapper.toUpdateTask(taskRequestDTO, id));

            return taskMapper.toTaskResponseDTO(taskUpdate);
    }

    public Page<TaskResponseDTO> findAll(Pageable pageable) {

        Page<Task> taskPage = taskRepository.findAll(pageable);

        return taskPage.map(taskMapper::toTaskResponseDTO);
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));
        return taskMapper.toTaskResponseDTO(task);
    }

    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Task", id));
        taskRepository.deleteById(id);
    }
}
