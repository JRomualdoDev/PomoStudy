package com.pomoStudy.controller;

import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.Task.TaskResponseDTO;
import com.pomoStudy.entity.Task;
import com.pomoStudy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/task")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO) {

        TaskResponseDTO taskResponseDTO = taskService.save(taskRequestDTO);

        return ResponseEntity.status(201).body(taskResponseDTO);
    }
}
