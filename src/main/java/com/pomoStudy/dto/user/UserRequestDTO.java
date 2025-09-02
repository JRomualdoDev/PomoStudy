package com.pomoStudy.dto.user;

import com.pomoStudy.entity.User;
import jakarta.validation.constraints.*;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

public record UserRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Password is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial")
        String password,

        @NotNull(message = "CreatedAt is required")
        @Future(message = "CreatedAt must be in the future")
        OffsetDateTime createdAt ) {


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
//
//    public void setCreatedAt(OffsetDateTime now) {
//        createdAt = now;
//    }
}
