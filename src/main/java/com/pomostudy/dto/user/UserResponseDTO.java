package com.pomostudy.dto.user;

import com.pomostudy.entity.User;

public record UserResponseDTO(Long id, String name, String email) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
