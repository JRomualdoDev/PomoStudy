package com.pomoStudy.controller;

import com.pomoStudy.dto.Goal.GoalRequestDTO;
import com.pomoStudy.dto.Goal.GoalResponseDTO;
import com.pomoStudy.entity.Goal;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/goal")
public class GoalController {

    @Autowired
    GoalService goalService;

    @PostMapping
    public ResponseEntity<String> createGoal(@RequestBody GoalRequestDTO goalRequestDTO) {

        goalService.save(goalRequestDTO);

        return ResponseEntity.status(201).body("Goal created with success");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> editGoal(@RequestBody GoalRequestDTO goalRequestDTO, @PathVariable("id") Long id) {

        goalService.edit(goalRequestDTO, id);

        return ResponseEntity.ok("Goal edited with success");
    }

    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> findAll() {
        List<Goal> goals = goalService.findAll();
        List<GoalResponseDTO> responseDTO = goals.stream()
                .map(GoalResponseDTO::new)
                .toList();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<GoalResponseDTO> findByID(@PathVariable("id") Long id) {
        GoalResponseDTO goalResponseDTO = goalService.findById(id)
                .map(GoalResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        return ResponseEntity.ok(goalResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        goalService.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Goal", id));
        goalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
