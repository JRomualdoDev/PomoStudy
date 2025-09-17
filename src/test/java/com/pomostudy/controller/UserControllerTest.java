package com.pomostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.user.UserRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.service.TokenService;
import com.pomostudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfigurations.class)
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private User user;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {

        userRequestDTO = new UserRequestDTO("testuser", "test@example.com", "Password@123", UserRole.ADMIN);
        userResponseDTO = new UserResponseDTO(1L, "testuser", "test@example.com");

        user = new User();
        user.setId(userId);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setRole(UserRole.ADMIN);

    }

//    @Test
//    @DisplayName("Should create a new user and return status 201 Created")
//    void shouldCreateUser_andReturn201() throws Exception {
//        // 1. Arrange
//        when(userService.save(any(UserRequestDTO.class))).thenReturn(userResponseDTO);
//        when(tokenService.validateToken(anyString())).thenReturn(String.valueOf(true));
//
//        // 2. Act & Assert
//        mockMvc.perform(post("/api/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestDTO))
//                        .header("Authorization", "testToken")
//                )
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("testuser"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//    }

//    @Test
//    @DisplayName("Should be return 400 Bad Request for when create a new user with invalid data")
//    void shouldReturn400_whenCreateUserWithInvalidData() throws Exception {
//
//        UserRequestDTO invalidUserDto = new UserRequestDTO(null, "invalid-email", "pass", UserRole.ADMIN);
//
//        mockMvc.perform(post("/api/user")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(invalidUserDto)))
//                    .andExpect(status().isBadRequest());
//    }


    @Test
    @DisplayName("Should be edit the existent user and return status 200 OK")
    void shouldEditUser_andReturn200() throws Exception {

        when(userService.edit(any(UserRequestDTO.class), anyLong())).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

    }

    @Test
    @DisplayName("Should be return 404 Not Found for try edit the non-existent user")
    void shouldReturn404_whenEditNonExistentUser() throws Exception {

        when(userService.edit(any(UserRequestDTO.class), anyLong()))
                .thenThrow(ResourceExceptionFactory.notFound("User", userId));

        mockMvc.perform(put("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should be return all the users with status 200 OK")
    void shouldFindAllUsers_andReturn200() throws Exception {

        List<User> users = Arrays.asList(user, new User());
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testuser"));
    }

    @Test
    @DisplayName("Should be found one user for name and email and return status 200 OK")
    void shouldFindUserById_andReturn200() throws Exception {

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should be return 404 Not Found when the User ID does not exist")
    void shouldReturn404_whenUserNotFoundById() throws Exception {

        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/user/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete the user and return status 204 No Content")
    void shouldDeleteUser_andReturn204() throws Exception {

        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userService).delete(anyLong());

        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should be return 404 Not Found for try delete the non-existent user")
    void shouldReturn404_whenDeleteNonExistentUser() throws Exception {

        when(userService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/user/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}