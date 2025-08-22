package com.pomoStudy.dto.Goal;

import com.pomoStudy.entity.User;
import com.pomoStudy.enums.GoalType;

import java.time.OffsetDateTime;

public record GoalRequestDTO(String title, String description, GoalType type,
                             Integer goalValue, Integer goalActual, OffsetDateTime endDate,
                             Boolean active, Long user_goal) {
}
