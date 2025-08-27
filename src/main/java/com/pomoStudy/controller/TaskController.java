package com.pomoStudy.controller;

import com.pomoStudy.dto.Task.TaskRequestDTO;
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

    @Autowired
    TaskService taskService;

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskRequestDTO taskRequestDTO) {

        taskService.save(taskRequestDTO);

        return ResponseEntity.status(201).body("Task created with success.");
    }
}
