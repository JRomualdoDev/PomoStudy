package com.pomoStudy.dto.Task.Validators.Priority;

import com.pomoStudy.enums.TaskPriority;
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


