package com.pomoStudy.controller;

import com.pomoStudy.dto.UserDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserDTO userDto) {

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());

        user.setCreatedAt(OffsetDateTime.now());

        userService.save(user);

        return ResponseEntity.ok(user.getName());
    }

}
