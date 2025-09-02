package com.pomoStudy.dto.Goal.Validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TypeValidator.class)
public @interface ValidType {
    String message() default "Goal Type must be DAYLY_TIME, WEEKLY_TIME, POMODORO_DAILY and TASKS_COMPLETED";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
