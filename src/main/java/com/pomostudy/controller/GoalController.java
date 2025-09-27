package com.pomostudy.controller;

import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.ErrorResponseDTO;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.GoalService;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/goal")
@Tag(name = "goal")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class GoalController {

    final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    @Operation(summary = "Create data goal", description = "Method for create data goal")
    @ApiResponse(responseCode = "201", description = "Goal created with success")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {

        GoalResponseDTO goalResponseDTO = goalService.save(goalRequestDTO);

        return ResponseEntity.status(201).body(goalResponseDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data goal", description = "Method for edit data goal")
    @ApiResponse(responseCode = "200", description = "Goal edited with success")
    @ApiResponse(responseCode = "404", description = "Goal not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<GoalResponseDTO> editGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO, @PathVariable("id") Long id) {

        GoalResponseDTO goalResponseDTO = goalService.edit(goalRequestDTO, id);

        return ResponseEntity.ok(goalResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data goal", description = "Method for list data goal")
    @ApiResponse(responseCode = "200", description = "Goal listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<Page<GoalResponseDTO>> findAllGoals(
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
    ){
        Page<GoalResponseDTO> listGoals = goalService.findAll(pageable);

        return ResponseEntity.ok(listGoals);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data goal for id", description = "Method for search data goal for the id")
    @ApiResponse(responseCode = "200", description = "Goal listed successfully")
    @ApiResponse(responseCode = "404", description = "Goal id not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<GoalResponseDTO> findGoalById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(goalService.findById(id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data goal for id", description = "Method for deleting data goal for the id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Goal not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<String> deleteGoal(@PathVariable("id") Long id) {

        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
