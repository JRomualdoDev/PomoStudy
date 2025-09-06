package com.pomoStudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomoStudy.dto.user.UserRequestDTO;
import com.pomoStudy.dto.user.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("testuser", "test@example.com", "password123");
        userResponseDTO = new UserResponseDTO("testuser", "test@example.com");
    }

    @Test
    @DisplayName("Should create a new user and return status 201 Created")
    void shouldCreateUser_andReturn201() throws Exception {
        // 1. Arrange
        when(userService.save(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // 2. Act & Assert
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testuser"))  // Corrigido de username para name
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

//    @Test
//    @DisplayName("Deve retornar 400 Bad Request ao tentar criar usuário com dados inválidos")
//    void shouldReturn400_whenCreateUserWithInvalidData() throws Exception {
//        // Arrange
//        UserRequestDTO invalidUserDto = new UserRequestDTO(null, "invalid-email", "pass");
//
//        // Act & Assert
//        mockMvc.perform(post("/api/user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidUserDto)))
//                .andExpect(status().isBadRequest()); // Verifica se o status HTTP é 400.
//    }
//
//
//    @Test
//    @DisplayName("Deve editar um usuário existente e retornar status 200 OK")
//    void shouldEditUser_andReturn200() throws Exception {
//        // Arrange
//        when(userService.edit(any(UserRequestDTO.class), anyLong())).thenReturn(userResponseDTO);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/user/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId));
//    }
//
//    @Test
//    @DisplayName("Deve retornar 404 Not Found ao tentar editar um usuário inexistente")
//    void shouldReturn404_whenEditNonExistentUser() throws Exception {
//        // Arrange
//        when(userService.edit(any(UserRequestDTO.class), anyLong()))
//                .thenThrow(ResourceExceptionFactory.notFound("User", userId));
//
//        // Act & Assert
//        mockMvc.perform(put("/api/user/{id}", userId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userRequestDTO)))
//                .andExpect(status().isNotFound()); // Verifica o status 404.
//    }
//
//    @Test
//    @DisplayName("Deve retornar todos os usuários com status 200 OK")
//    void shouldFindAllUsers_andReturn200() throws Exception {
//        // Arrange
//        List<User> users = Arrays.asList(user, new User()); // Lista de usuários para simular o retorno.
//        when(userService.findAll()).thenReturn(users);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/user"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].username").value("john.doe"));
//    }
//
//    @Test
//    @DisplayName("Deve encontrar um usuário pelo ID e retornar status 200 OK")
//    void shouldFindUserById_andReturn200() throws Exception {
//        // Arrange
//        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
//
//        // Act & Assert
//        mockMvc.perform(get("/api/user/{id}", userId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userId));
//    }
//
//    @Test
//    @DisplayName("Deve retornar 404 Not Found quando o ID do usuário não existe")
//    void shouldReturn404_whenUserNotFoundById() throws Exception {
//        // Arrange
//        when(userService.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        mockMvc.perform(get("/api/user/{id}", 99L))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @DisplayName("Deve deletar um usuário e retornar status 204 No Content")
//    void shouldDeleteUser_andReturn204() throws Exception {
//        // Arrange
//        when(userService.findById(anyLong())).thenReturn(Optional.of(user));
//        doNothing().when(userService).delete(anyLong()); // Para métodos `void`, use `doNothing`.
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/user/{id}", userId))
//                .andExpect(status().isNoContent()); // Verifica o status 204.
//    }
//
//    @Test
//    @DisplayName("Deve retornar 404 Not Found ao tentar deletar um usuário inexistente")
//    void shouldReturn404_whenDeleteNonExistentUser() throws Exception {
//        // Arrange
//        when(userService.findById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/user/{id}", 99L))
//                .andExpect(status().isNotFound());
//    }
}