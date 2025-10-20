package com.pomostudy.dto.task.validators.status_task;

import jakarta.validation.ConstraintValidator;
import com.pomostudy.enums.StatusTask;
import jakarta.validation.ConstraintValidatorContext;

public class StatusTaskValidator implements ConstraintValidator<ValidStatusTask, StatusTask> {
    @Override
    public boolean isValid(StatusTask statusTask, ConstraintValidatorContext constraintValidatorContext) {

        if (statusTask == null) return true;

        try {
            StatusTask.valueOf(statusTask.name());
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }
}
