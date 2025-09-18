package com.pomostudy.dto.user;

import jakarta.validation.constraints.*;

public record UserUpdateRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotNull(message = "Password is required")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "The password should be contains at least one uppercase, one lowercase, one number and one special character")
        String password

) {

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
