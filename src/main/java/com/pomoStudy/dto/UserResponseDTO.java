package com.pomoStudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomoStudy.entity.User;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

public class UserResponseDTO {

    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private String email;
    private OffsetDateTime createdAt;

    public UserResponseDTO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public UserResponseDTO() {
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setCreatedAt(OffsetDateTime now) {
    }
}
