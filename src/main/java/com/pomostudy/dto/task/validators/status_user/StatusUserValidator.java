package com.pomostudy.dto.task.validators.status_user;

import jakarta.validation.ConstraintValidator;
import com.pomostudy.enums.StatusUser;
import jakarta.validation.ConstraintValidatorContext;

public class StatusUserValidator implements ConstraintValidator<ValidStatusUser, StatusUser> {
    @Override
    public boolean isValid(StatusUser statusUser, ConstraintValidatorContext constraintValidatorContext) {

        if (statusUser == null) return true;

        try {
            StatusUser.valueOf(statusUser.name());
        } catch (IllegalArgumentException ex) {
            return false;
        }

        return true;
    }
}
