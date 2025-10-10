package com.pomostudy.dto.task;

import com.pomostudy.dto.task.validators.priority.ValidPriority;
import com.pomostudy.dto.task.validators.status_user.ValidStatusUser;
import com.pomostudy.enums.StatusUser;
import com.pomostudy.enums.TaskPriority;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

public record TaskRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Start date is required")
        @Future(message = "Start date must be in the future")
        OffsetDateTime startDate,

        @NotNull(message = "End date is required")
        @Future(message = "Start date must be in the future")
        OffsetDateTime endDate,

        @NotNull(message = "Status is required")
        @ValidStatusUser
        StatusUser status,

        @NotNull(message = "Priority is required")
        @ValidPriority
        TaskPriority priority,

        Integer timeTotalLearning,

        @NotNull(message = "Category ID is required")
        Long categoryId ) {
}
