package com.pomostudy.dto.task.validators.priority;

import com.pomostudy.enums.TaskPriority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PriorityValidator implements ConstraintValidator<ValidPriority, TaskPriority> {
    @Override
    public boolean isValid(TaskPriority taskPriority, ConstraintValidatorContext constraintValidatorContext) {

        if (taskPriority == null) return true;

        try {
            TaskPriority.valueOf(taskPriority.name());
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }
}


