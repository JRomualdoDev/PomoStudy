package com.pomostudy.dto.user;

import com.pomostudy.entity.User;

public record UserResponseDTO(String name, String email) {

    public UserResponseDTO(User user) {
        this(user.getName(), user.getEmail());
    }
}
