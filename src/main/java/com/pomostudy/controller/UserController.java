package com.pomostudy.controller;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.ErrorResponseDTO;
import com.pomostudy.dto.PaginationDTO;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Tag(name = "user")
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
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<UserResponseDTO> editUser(
            @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        UserResponseDTO userResponseDTO = userService.edit(userUpdateRequestDTO, authenticatedUser, id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data user", description = "Method for list data user")
    @ApiResponse(responseCode = "200", description = "User listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<PaginationDTO<UserResponseDTO>> findAllUsers(
            @Parameter(
                    name = "pageable",
                    in = ParameterIn.QUERY,
                    description = "Pagination and sorting object. **To use default values, send an empty object: `{}`**.",
                    examples = {
                            @ExampleObject(
                                    name = "Default Pagination",
                                    summary = "Fetch with default values",
                                    value = "{}"
                            ),
                            @ExampleObject(
                                    name = "Custom Pagination",
                                    summary = "Fetching the first page with 10 items",
                                    value = "{\"page\": 0, \"size\": 10, \"sort\": \"name,asc\"}"
                            )
                    },
                    schema = @Schema(type = "object")
            )
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {
        Page<UserResponseDTO> listUsers = userService.findAll(pageable, authenticatedUser);

        return ResponseEntity.ok(new PaginationDTO<>(listUsers));
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data user for id", description = "Method for search data user for the id")
    @ApiResponse(responseCode = "200", description = "User listed successfully")
    @ApiResponse(responseCode = "404", description = "User id not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<Object> findById(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return ResponseEntity.ok(userService.findById(id, authenticatedUser));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data user for id", description = "Method for deleting data user for the id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<String> delete(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        userService.delete(id, authenticatedUser);
        return ResponseEntity.noContent().build();
    }
}
