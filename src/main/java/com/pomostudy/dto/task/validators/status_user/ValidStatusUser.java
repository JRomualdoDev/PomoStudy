package com.pomostudy.dto.task.validators.status_user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StatusUserValidator.class)
public @interface ValidStatusUser {
    String message() default "Status must be PENDING, IN_PROGRESS, COMPLETED or CANCELED";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
