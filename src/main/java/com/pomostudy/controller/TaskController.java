package com.pomostudy.controller;

import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "task", description = "Controller for saving, edit, search and delete task.")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create data task", description = "Method for create data task")
    @ApiResponse(responseCode = "201", description = "Task created with success")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, UriComponentsBuilder ucb) {

        TaskResponseDTO taskResponseDTO = taskService.save(taskRequestDTO);

        URI locationOfNewtask = ucb
                .path("api/task/{id}")
                .buildAndExpand(taskResponseDTO.id())
                .toUri();

        return ResponseEntity.created(locationOfNewtask).body(taskResponseDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data task", description = "Method for edit data task")
    @ApiResponse(responseCode = "200", description = "Task edited with success")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<TaskResponseDTO> editTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, @PathVariable("id") Long id) {

        TaskResponseDTO taskResponseDTO = taskService.edit(taskRequestDTO, id);

        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data task", description = "Method for list data task")
    @ApiResponse(responseCode = "200", description = "Task listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Page<TaskResponseDTO>> findAllTasks(
            @Parameter(
                    name = "pageable",
                    in = ParameterIn.QUERY,
                    description = "Pagination and sorting object. **To use default values, send an empty object: `{}`**.",
                    examples = {
                            @ExampleObject(
                                    name = "Default Pagination",
                                    summary = "Fetch with default values",
                                    value = "{}"
                            ),
                            @ExampleObject(
                                    name = "Custom Pagination",
                                    summary = "Fetching the first page with 10 items",
                                    value = "{\"page\": 0, \"size\": 10, \"sort\": \"name,asc\"}"
                            )
                    },
                    schema = @Schema(type = "object")
            )
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
            ) {

        Page<TaskResponseDTO> listTasks = taskService.findAll(pageable);

        return ResponseEntity.ok(listTasks);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data task for id", description = "Method for search data task for the id")
    @ApiResponse(responseCode = "200", description = "Task listed successfully")
    @ApiResponse(responseCode = "404", description = "Task id not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<TaskResponseDTO> findTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data task for id", description = "Method for deleting data task for the id")
    @ApiResponse(responseCode = "204", description = "Task editing with success")
    @ApiResponse(responseCode = "404", description = "Task not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
