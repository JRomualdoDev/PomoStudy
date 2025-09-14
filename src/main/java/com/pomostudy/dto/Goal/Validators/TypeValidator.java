package com.pomostudy.dto.Goal.Validators;

import com.pomostudy.enums.GoalType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TypeValidator implements ConstraintValidator<ValidType, GoalType> {
    @Override
    public boolean isValid(GoalType goalType, ConstraintValidatorContext constraintValidatorContext) {

        if (goalType == null) return true;

        try {
            GoalType.valueOf(goalType.name());
        } catch (IllegalArgumentException ex) {
            return false;
        }
        return true;
    }
}
