package com.pomoStudy.dto.Task.Validators.Priority;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PriorityValidator.class)
public @interface ValidPriority {
    String message() default "Priority must be LOW, MEDIUM or HIGH";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
