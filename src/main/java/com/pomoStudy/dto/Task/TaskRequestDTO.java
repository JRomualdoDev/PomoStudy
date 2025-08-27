package com.pomoStudy.dto.Task;

import com.pomoStudy.entity.Category;
import com.pomoStudy.enums.StatusUser;
import com.pomoStudy.enums.TaskPriority;

import java.time.OffsetDateTime;

public record TaskRequestDTO(String name, String description, OffsetDateTime startDate,
                             OffsetDateTime endDate, StatusUser status, TaskPriority priority,
                             Integer timeTotalLearning, Long user_task, Long categoryId) {
}
