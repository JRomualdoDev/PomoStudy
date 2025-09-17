package com.pomostudy.controller;

import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.goal.GoalRequestDTO;
import com.pomostudy.dto.goal.GoalResponseDTO;
import com.pomostudy.entity.Goal;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/goal")
@Tag(name = "goal", description = "Controller for saving, edit, search and delete goal.")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class GoalController {

    final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    @Operation(summary = "Create data goal", description = "Method for create data goal")
    @ApiResponse(responseCode = "201", description = "Goal created with success")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {

        GoalResponseDTO goalResponseDTO = goalService.save(goalRequestDTO);

        return ResponseEntity.status(201).body(goalResponseDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data goal", description = "Method for edit data goal")
    @ApiResponse(responseCode = "200", description = "Goal edited with success")
    @ApiResponse(responseCode = "404", description = "Goal not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<GoalResponseDTO> editGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO, @PathVariable("id") Long id) {

        GoalResponseDTO goalResponseDTO = goalService.edit(goalRequestDTO, id);

        return ResponseEntity.ok(goalResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data goal", description = "Method for list data goal")
    @ApiResponse(responseCode = "200", description = "Goal listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<GoalResponseDTO>> findAllGoals() {
        List<Goal> goals = goalService.findAll();
        List<GoalResponseDTO> responseDTO = goals.stream()
                .map(GoalResponseDTO::new)
                .toList();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data goal for id", description = "Method for search data goal for the id")
    @ApiResponse(responseCode = "200", description = "Goal listed successfully")
    @ApiResponse(responseCode = "404", description = "Goal id not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<GoalResponseDTO> findGoalById(@PathVariable("id") Long id) {
        GoalResponseDTO goalResponseDTO = goalService.findById(id)
                .map(GoalResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        return ResponseEntity.ok(goalResponseDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data goal for id", description = "Method for deleting data goal for the id")
    @ApiResponse(responseCode = "204", description = "Goal editing with success")
    @ApiResponse(responseCode = "404", description = "Goal not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> deleteGoal(@PathVariable("id") Long id) {
        goalService.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
