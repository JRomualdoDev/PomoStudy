package com.pomoStudy.controller;

import com.pomoStudy.dto.user.TaggingInterface.OnCreate;
import com.pomoStudy.dto.user.UserRequestDTO;
import com.pomoStudy.dto.user.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Validated(OnCreate.class) @RequestBody UserRequestDTO userDto) {

        UserResponseDTO userResponseDTO = userService.save(userDto);

        return ResponseEntity.status(201).body(userResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponseDTO> editUser(@Valid @RequestBody UserRequestDTO userRequestDTO, @PathVariable("id") Long id) {

        UserResponseDTO userResponseDTO = userService.edit(userRequestDTO, id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDTO> responseDTO = users
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        UserResponseDTO userResponseDTO = userService.findById(id)
                .map(UserResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));

        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
