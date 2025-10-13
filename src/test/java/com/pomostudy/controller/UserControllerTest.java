package com.pomostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.security.WithMockAuthenticatedUser;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfigurations.class)
@ActiveProfiles("test")
@WithMockAuthenticatedUser
class UserControllerTest {

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

    private UserCreateRequestDTO userCreateRequestDTO;
    private UserUpdateRequestDTO userUpdateRequestDTO;
    private UserResponseDTO userResponseDTO;
    private User user;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {

        userCreateRequestDTO = new UserCreateRequestDTO("testuser", "test@example.com", "Password@123");
        userUpdateRequestDTO = new UserUpdateRequestDTO("testuser", "Password@123");
        userResponseDTO = new UserResponseDTO(1L, "testuser", "test@example.com");

        user = new User();
        user.setId(userId);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setRole(UserRole.ADMIN);

    }

    @Test
    @DisplayName("Should be edit the user and return status 200 OK")
    void shouldEditUser_andReturn200() throws Exception {

        when(userService.edit(any(UserUpdateRequestDTO.class),any(AuthenticatedUser.class), anyLong())).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

    }

    @Test
    @DisplayName("Should be return 404 Not Found for try edit the non-existent user")
    void shouldReturn404_whenEditNonExistentUser() throws Exception {

        when(userService.edit(any(UserUpdateRequestDTO.class), any(AuthenticatedUser.class), anyLong()))
                .thenThrow(ResourceExceptionFactory.notFound("User", userId));

        mockMvc.perform(put("/api/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should be found one user for name and email and return status 200 OK")
    void shouldFindUserById_andReturn200() throws Exception {

        when(userService.findById(anyLong(), any(AuthenticatedUser.class))).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Should be return 404 Not Found when the User ID does not exist")
    void shouldReturn404_whenUserNotFoundById() throws Exception {

        when(userService.findById(anyLong(), any(AuthenticatedUser.class))).thenThrow(
                ResourceExceptionFactory.notFound("User", 99L)
        );

        mockMvc.perform(get("/api/user/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete the user and return status 204 No Content")
    void shouldDeleteUser_andReturn204() throws Exception {

        when(userService.findById(anyLong(), any(AuthenticatedUser.class))).thenReturn(userResponseDTO);
        doNothing().when(userService).delete(anyLong(), any(AuthenticatedUser.class));

        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(anyLong(), any(AuthenticatedUser.class));
    }

}