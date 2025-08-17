package com.pomoStudy.controller;

import com.pomoStudy.dto.user.UserRequestDTO;
import com.pomoStudy.dto.user.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userDto) {

        userService.save(userDto);

        return ResponseEntity.status(201).body("User created with success.");
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable("id") Long id) {

        userService.edit(userRequestDTO, id);
        return ResponseEntity.ok("User Edited with success.");
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
        Optional<User> user = userService.findById(id);
        Optional<Object> userResponseDTO = Optional.of(user.map(UserResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id)));

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
