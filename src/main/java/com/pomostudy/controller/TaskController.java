package com.pomostudy.controller;

import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<TaskResponseDTO>> findAllTasks(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
            ) {

        Page<TaskResponseDTO> listTasks = taskService.findAll(pageable);

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
