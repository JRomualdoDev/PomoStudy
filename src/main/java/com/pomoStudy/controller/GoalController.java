package com.pomoStudy.controller;

import com.pomoStudy.dto.Goal.GoalRequestDTO;
import com.pomoStudy.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/goal")
public class GoalController {

    @Autowired
    GoalService goalService;

    @PostMapping
    public ResponseEntity<String> createGoal(@RequestBody GoalRequestDTO goalRequestDTO) {

        System.out.println(goalRequestDTO);
        goalService.save(goalRequestDTO);

        return ResponseEntity.status(201).body("Goal created with success");
    }
}
