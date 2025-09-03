package com.pomoStudy.dto.user;

import com.pomoStudy.dto.user.TaggingInterface.OnCreate;
import jakarta.validation.constraints.*;

public record UserRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Email is required", groups = OnCreate.class)
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Password is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial")
        String password
//
//        @NotNull(message = "CreatedAt is required")
//        @Future(message = "CreatedAt must be in the future")
//        OffsetDateTime createdAt
) {


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
