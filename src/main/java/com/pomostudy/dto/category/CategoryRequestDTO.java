package com.pomostudy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 charaacters")
        String name,

        @NotBlank(message = "Color is required")
        String color,

        @NotBlank(message = "Icon is required")
        String icon,

        @NotNull(message = "User id is required")
        Long userId) {

}
