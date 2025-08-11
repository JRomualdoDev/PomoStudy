package com.pomoStudy.dto;

import java.time.OffsetDateTime;

public record UserDTO(String name, String email, String password, OffsetDateTime createdAT) {

    public String getName() {
        return name;
    }

    public void setCreatedAt(OffsetDateTime now) {
    }
}
