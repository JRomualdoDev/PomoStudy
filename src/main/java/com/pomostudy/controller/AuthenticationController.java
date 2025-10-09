package com.pomostudy.controller;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.ErrorResponseDTO;
import com.pomostudy.dto.auth.AuthenticationDTO;
import com.pomostudy.dto.auth.LoginResponseDTO;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.entity.User;
import com.pomostudy.service.TokenService;
import com.pomostudy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/auth")
@Tag(name = "authentication")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserService userService,
            TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Method for Login user")
    @ApiResponse(responseCode = "200", description = "User logged successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Credential invalid",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken emailPassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());

        var auth = this.authenticationManager.authenticate(emailPassword);

        var token = tokenService.generateToken((AuthenticatedUser) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Method for Register user")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Email already in use",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class)))
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserCreateRequestDTO userCreateRequestDTO, UriComponentsBuilder ucb) {

        UserResponseDTO userResponseDTO = userService.save(userCreateRequestDTO);

        URI locationOfNewUser = ucb
                .path("/api/user/{id}")
                .buildAndExpand(userResponseDTO.id())
                .toUri();


        return ResponseEntity.created(locationOfNewUser).body(userResponseDTO);
    }
}
