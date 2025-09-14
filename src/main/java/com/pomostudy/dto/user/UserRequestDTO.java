package com.pomostudy.dto.user;

import com.pomostudy.dto.user.TaggingInterface.OnCreate;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;

public record UserRequestDTO(

        @NotBlank(message = "Name is required", groups = {Default.class, OnCreate.class})
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters", groups = {Default.class, OnCreate.class})
        String name,

        @NotBlank(message = "Email is required", groups = OnCreate.class)
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Password is required", groups = {Default.class, OnCreate.class})
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
                message = "The password should be contains at least one uppercase, one lowercase, one number and one special character")
        String password
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
