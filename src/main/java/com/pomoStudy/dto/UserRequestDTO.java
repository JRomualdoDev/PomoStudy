package com.pomoStudy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pomoStudy.entity.User;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

public class UserRequestDTO {

//    @JsonProperty(required = true)
    private String name;
//    @JsonProperty(required = true)
    private String email;
//    @JsonProperty(required = true)
    private String password;

    public UserRequestDTO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public UserRequestDTO() {
    }

    public String getPassword() {
        return password;
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
