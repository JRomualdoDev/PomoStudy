package com.pomoStudy.dto.user;

import com.pomoStudy.entity.User;

public record UserResponseDTO(String name, String email) {

    public UserResponseDTO(User user) {
        this(user.getName(), user.getEmail());
    }
}
