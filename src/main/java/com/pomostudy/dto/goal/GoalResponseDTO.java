package com.pomostudy.dto.goal;

import com.pomostudy.entity.Goal;
import com.pomostudy.enums.GoalType;

import java.time.OffsetDateTime;

public record GoalResponseDTO(String title, String description, GoalType type,
                              Integer goalValue, Integer goalActual, OffsetDateTime endDate,
                              Boolean active) {

    public GoalResponseDTO(Goal goal) {
        this(goal.getTitle(), goal.getDescription(), goal.getType(), goal.getGoalValue(),
                goal.getGoalActual(), goal.getEndDate(), goal.getActive());
    }
}
