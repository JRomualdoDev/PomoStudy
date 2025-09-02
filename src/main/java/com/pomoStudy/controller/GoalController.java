package com.pomoStudy.controller;

import com.pomoStudy.dto.Goal.GoalRequestDTO;
import com.pomoStudy.dto.Goal.GoalResponseDTO;
import com.pomoStudy.entity.Goal;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/goal")
public class GoalController {

    final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GoalResponseDTO> createGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO) {

        GoalResponseDTO goalResponseDTO = goalService.save(goalRequestDTO);

        return ResponseEntity.status(201).body(goalResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<GoalResponseDTO> editGoal(@Valid @RequestBody GoalRequestDTO goalRequestDTO, @PathVariable("id") Long id) {

        GoalResponseDTO goalResponseDTO = goalService.edit(goalRequestDTO, id);

        return ResponseEntity.ok(goalResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> findAllGoals() {
        List<Goal> goals = goalService.findAll();
        List<GoalResponseDTO> responseDTO = goals.stream()
                .map(GoalResponseDTO::new)
                .toList();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<GoalResponseDTO> findGoalById(@PathVariable("id") Long id) {
        GoalResponseDTO goalResponseDTO = goalService.findById(id)
                .map(GoalResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        return ResponseEntity.ok(goalResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable("id") Long id) {
        goalService.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
