package com.pomoStudy.service;

import com.pomoStudy.dto.user.UserMapper;
import com.pomoStudy.dto.user.UserRequestDTO;
import com.pomoStudy.dto.user.UserResponseDTO;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceException;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO("testuser", "test@example.com", "password123");
        userResponseDTO = new UserResponseDTO("testuser", "test@example.com");
        user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void shouldSaveUserSuccessfully() {

        when(userMapper.toUser(any(UserRequestDTO.class), eq(null))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.save(userRequestDTO);

        assertNotNull(result);
        assertEquals("testuser", result.name());
        assertEquals("test@example.com", result.email());

        verify(userMapper, times(1)).toUser(any(UserRequestDTO.class), eq(null));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).userResponseDTO(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingWithDuplicateData() {

        when(userMapper.toUser(any(UserRequestDTO.class), eq(null))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> userService.save(userRequestDTO));

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, never()).userResponseDTO(any(User.class));
    }

    @Test
    void shouldEditUserSuccessfully() {
        Long userId = 1L;
        when(userMapper.toUser(userRequestDTO, userId)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.edit(userRequestDTO, userId);

        assertNotNull(result);
        assertEquals(userResponseDTO.name(), result.name());
        assertEquals(userResponseDTO.email(), result.email());

        verify(userMapper, times(1)).toUser(userRequestDTO, userId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).userResponseDTO(user);
    }

    @Test
    void shouldThrowResourceExceptionWhenEditingNonExistentUser() {
        Long userId = 99L;
        when(userMapper.toUser(userRequestDTO, userId)).thenThrow(ResourceExceptionFactory.notFound("User", userId));

        ResourceException thrown = assertThrows(ResourceException.class, () -> userService.edit(userRequestDTO, userId));

        assertEquals("User with id 99 not found.", thrown.getMessage());

        verify(userMapper, times(1)).toUser(userRequestDTO, userId);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void shouldFindAllUsers() {

        List<User> userList = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(user, result.getFirst());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldFindUserByIdWhenExists() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserDoesNotExist() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(userId);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}