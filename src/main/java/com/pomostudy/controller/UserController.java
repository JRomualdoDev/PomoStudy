package com.pomostudy.controller;

import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Tag(name = "user", description = "Controller for saving, edit, search and delete user.")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<UserResponseDTO> createUser(@Validated(OnCreate.class) @RequestBody UserCreateRequestDTO userDto) {
//
//        UserResponseDTO userResponseDTO = userService.save(userDto);
//
//        return ResponseEntity.status(201).body(userResponseDTO);
//    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data user", description = "Method for editing data user")
    @ApiResponse(responseCode = "200", description = "User editing with success")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<UserResponseDTO> editUser(@Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO, @PathVariable("id") Long id) {

        UserResponseDTO userResponseDTO = userService.edit(userUpdateRequestDTO, id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data user", description = "Method for list data user")
    @ApiResponse(responseCode = "200", description = "User listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDTO> responseDTO = users
                .stream()
                .map(UserResponseDTO::new)
                .toList();

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data user for id", description = "Method for search data user for the id")
    @ApiResponse(responseCode = "200", description = "User listed successfully")
    @ApiResponse(responseCode = "404", description = "User id not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Object> findById(@PathVariable("id") Long id) {
        UserResponseDTO userResponseDTO = userService.findById(id)
                .map(UserResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));

        return ResponseEntity.ok(userResponseDTO);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data user for id", description = "Method for deleting data user for the id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        userService.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("User", id));
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
