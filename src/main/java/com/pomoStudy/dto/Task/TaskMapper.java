package com.pomoStudy.dto.Task;

import com.pomoStudy.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponseDTO(Task task) {
       return new TaskResponseDTO(
                task.getName(),
                task.getDescription(),
                task.getStartDate(),
                task.getEndDate(),
                task.getStatus(),
                task.getPriority(),
                task.getTimeTotalLearning(),
                task.getUser_task().getId(),
                task.getCategory().getId()
        );
    }
}
