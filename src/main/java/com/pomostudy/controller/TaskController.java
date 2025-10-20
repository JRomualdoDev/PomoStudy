package com.pomostudy.controller;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.ErrorResponseDTO;
import com.pomostudy.dto.PaginationDTO;
import com.pomostudy.dto.task.TaskRequestDTO;
import com.pomostudy.dto.task.TaskResponseDTO;
import com.pomostudy.dto.task.TaskResponseMonthDTO;
import com.pomostudy.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/task")
@Tag(name = "task")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create data task", description = "Method for create data task")
    @ApiResponse(responseCode = "201", description = "Task created with success")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid
            @RequestBody TaskRequestDTO taskRequestDTO,
            UriComponentsBuilder ucb,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        TaskResponseDTO taskResponseDTO = taskService.save(taskRequestDTO, authenticatedUser);

        URI locationOfNewTask = ucb
                .path("api/task/{id}")
                .buildAndExpand(taskResponseDTO.id())
                .toUri();

        return ResponseEntity.created(locationOfNewTask).body(taskResponseDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data task", description = "Method for edit data task")
    @ApiResponse(responseCode = "200", description = "Task edited with success")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TaskResponseDTO> editTask(
            @Valid
            @RequestBody TaskRequestDTO taskRequestDTO,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        TaskResponseDTO taskResponseDTO = taskService.edit(taskRequestDTO, authenticatedUser, id);

        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data task", description = "Method for list data task")
    @ApiResponse(responseCode = "200", description = "Task listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<PaginationDTO<TaskResponseDTO>> findAllTasks(
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
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
            ) {

        Page<TaskResponseDTO> listTasks = taskService.findAll(pageable, authenticatedUser, categoryId);

        return ResponseEntity.ok(new PaginationDTO<>(listTasks));
    }

    @GetMapping("month/{month}")
    @Operation(summary = "List all data task per month", description = "Method for list data task per month")
    @ApiResponse(responseCode = "200", description = "Task listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<PaginationDTO<TaskResponseMonthDTO>> findAllTaskByMonth(
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
            Pageable pageable,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable("month") String month
            ) {

        Page<TaskResponseMonthDTO> listTasks = taskService.findAllTaskByMonth(pageable, authenticatedUser, month);

        return ResponseEntity.ok(new PaginationDTO<>(listTasks));
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data task for id", description = "Method for search data task for the id")
    @ApiResponse(responseCode = "200", description = "Task listed successfully")
    @ApiResponse(responseCode = "404", description = "Task id not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<TaskResponseDTO> findTaskById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return ResponseEntity.ok(taskService.findById(id, authenticatedUser));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data task for id", description = "Method for deleting data task for the id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Task not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<String> deleteTask(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        taskService.delete(id,authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}
