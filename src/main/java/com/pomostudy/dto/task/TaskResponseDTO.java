package com.pomostudy.dto.task;

import com.pomostudy.entity.Task;
import com.pomostudy.enums.StatusUser;
import com.pomostudy.enums.TaskPriority;

import java.time.OffsetDateTime;

public record TaskResponseDTO(
        Long id,
        String name,
        String description,
        OffsetDateTime startDate,
        OffsetDateTime endDate,
        StatusUser status,
        TaskPriority priority,
        Integer timeTotalLearning,
        Long user_task,
        Long categoryId) {

    public TaskResponseDTO(Task task) {
        this(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getTimeTotalLearning(),
                task.getUser_task().getId(),
                task.getCategory().getId());
    }
}
