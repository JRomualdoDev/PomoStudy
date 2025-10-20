package com.pomostudy.dto.task;

import com.pomostudy.entity.Task;

import java.time.OffsetDateTime;

public record TaskResponseMonthDTO(
        Long id,
        String name,
        OffsetDateTime startDate
) {

    public TaskResponseMonthDTO(Task task) {
        this(
                task.getId(),
                task.getName(),
                task.getStartDate()
        );
    }
}
