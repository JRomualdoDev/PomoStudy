package com.pomoStudy.controller;

import com.pomoStudy.dto.UserRequestDTO;
import com.pomoStudy.dto.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userDto) {

        userService.save(userDto);

        return ResponseEntity.ok("User created with success.");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable("id") Long id) {

        userService.edit(userRequestDTO, id);
        return ResponseEntity.ok("User Edited with success.");

    }
}
