package com.pomostudy.dto.Goal;

import com.pomostudy.dto.Goal.Validators.ValidType;
import com.pomostudy.enums.GoalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record GoalRequestDTO(

        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Size(max = 255, message = "Description must be max 255 characters")
        String description,

        @NotNull(message = "Type is required")
        @ValidType
        GoalType type,

        @NotNull(message = "Goal value is required")
        Integer goalValue,

        @NotNull(message = "Goal actual is required")
        Integer goalActual,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        OffsetDateTime endDate,

        @NotNull(message = "Active is required")
        Boolean active,

        @NotNull(message = "User goal is required")
        Long user_goal) {
}
