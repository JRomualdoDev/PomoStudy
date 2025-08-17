package com.pomoStudy.dto.user;

import com.pomoStudy.entity.User;

public class UserResponseDTO {

    private String name;
    private String email;

    public UserResponseDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
