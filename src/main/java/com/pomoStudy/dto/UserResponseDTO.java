package com.pomoStudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomoStudy.entity.User;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

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
