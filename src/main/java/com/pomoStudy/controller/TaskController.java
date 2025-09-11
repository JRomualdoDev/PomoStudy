package com.pomoStudy.controller;

import com.pomoStudy.dto.Task.TaskRequestDTO;
import com.pomoStudy.dto.Task.TaskResponseDTO;
import com.pomoStudy.entity.Task;
import com.pomoStudy.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, UriComponentsBuilder ucb) {

        TaskResponseDTO taskResponseDTO = taskService.save(taskRequestDTO);

        URI locationOfNewtask = ucb
                .path("api/task/{id}")
                .buildAndExpand(taskResponseDTO.id())
                .toUri();

        return ResponseEntity.created(locationOfNewtask).body(taskResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskResponseDTO> editTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, @PathVariable("id") Long id) {

        TaskResponseDTO taskResponseDTO = taskService.edit(taskRequestDTO, id);

        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAllTasks() {

        List<TaskResponseDTO> listTasks = taskService.findAll();

        return ResponseEntity.ok(listTasks);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskResponseDTO> findTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
