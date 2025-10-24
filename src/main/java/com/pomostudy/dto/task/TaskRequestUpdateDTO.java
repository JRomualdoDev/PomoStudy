package com.pomostudy.dto.task;

import com.pomostudy.dto.task.validators.priority.ValidPriority;
import com.pomostudy.dto.task.validators.status_task.ValidStatusTask;
import com.pomostudy.enums.StatusTask;
import com.pomostudy.enums.TaskPriority;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record TaskRequestUpdateDTO(
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        OffsetDateTime startDate,

        OffsetDateTime endDate,

        @ValidStatusTask
        StatusTask status,

        @ValidPriority
        TaskPriority priority,

        Integer timeTotalLearning,

        Long categoryId ) {
}
