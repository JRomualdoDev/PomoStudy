package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.mapper.UserMapper;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.dto.user.UserUpdateRequestDTO;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private UserService userService;

    private UserCreateRequestDTO userCreateRequestDTO;
    private UserUpdateRequestDTO userUpdateRequestDTO;
    private UserResponseDTO userResponseDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateRequestDTO = new UserCreateRequestDTO("testuser", "test@example.com", "password123");
        userUpdateRequestDTO = new UserUpdateRequestDTO("testuser", "password123");
        userResponseDTO = new UserResponseDTO(1L,"testuser", "test@example.com");
        user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should save user with success in the db")
    void shouldSaveUserSuccessfully() {

        when(userMapper.toCreateUser(any(UserCreateRequestDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.save(userCreateRequestDTO);

        assertNotNull(result);
        assertEquals("testuser", result.name());
        assertEquals("test@example.com", result.email());

        verify(userMapper, times(1)).toCreateUser(any(UserCreateRequestDTO.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toUserResponseDTO(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when save email is already in the db")
    void shouldThrowExceptionWhenSavingWithDuplicateData() {

        when(userMapper.toCreateUser(any(UserCreateRequestDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> userService.save(userCreateRequestDTO));

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, never()).toUserResponseDTO(any(User.class));
    }

    @Test
    @DisplayName("Should edit user successfully in the db")
    void shouldEditUserSuccessfully() {
        Long userId = 1L;

        when(userMapper.toUpdateUser(userUpdateRequestDTO, authenticatedUser, userId)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.edit(userUpdateRequestDTO, authenticatedUser, userId);

        assertNotNull(result);
        assertEquals(userResponseDTO.name(), result.name());
        assertEquals(userResponseDTO.email(), result.email());

        verify(userMapper, times(1)).toUpdateUser(userUpdateRequestDTO, authenticatedUser, userId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toUserResponseDTO(user);
    }

    @Test
    @DisplayName("Should throw exception when edit user that not exist in the db")
    void shouldThrowResourceExceptionWhenEditingNonExistentUser() {
        Long userId = 99L;
        when(userMapper.toUpdateUser(userUpdateRequestDTO, authenticatedUser, userId)).thenThrow(ResourceExceptionFactory.notFound("User", userId));

        ResourceException error = assertThrows(ResourceException.class, () -> userService.edit(userUpdateRequestDTO, authenticatedUser, userId));

        assertEquals("User with id 99 not found.", error.getMessage());

        verify(userMapper, times(1)).toUpdateUser(userUpdateRequestDTO, authenticatedUser, userId);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Should find all user for an ADMIN user and return 200 OK")
    void shouldFindAllUsersForAdmin() {

        List<User> userResponse = Collections.singletonList(user);
        Pageable pageable = PageRequest.of(0, 10);


        when(authenticatedUser.isAdmin()).thenReturn(true);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(userResponse));
        when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);


        Page<UserResponseDTO> result = userService.findAll(pageable, authenticatedUser);


        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should find users for a NON-ADMIN user and return 200 OK")
    void shouldFindAllUsersDBWithStatus200() {

        Pageable pageable = PageRequest.of(0,10);

        when(authenticatedUser.getUser()).thenReturn(user);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserResponseDTO result = userService.findById(user.getId(), authenticatedUser);

        assertNotNull(result);
        assertEquals(userResponseDTO.name(), result.name());
        assertEquals(userResponseDTO.email(), result.email());

        verify(userRepository, never()).findAll(pageable);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    @DisplayName("Should find the user from id when user exists in the db")
    void shouldFindUserByIdWhenExists() {
        Long userId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(userId, authenticatedUser);

        assertNotNull(result);
        assertEquals(userResponseDTO.name(), result.name());
        assertEquals(userResponseDTO.email(), result.email());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should return Exception when user does not exist int the db")
    void shouldReturnExceptionWhenUserDoesNotExist() {
        Long userId = 99L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenThrow(ResourceExceptionFactory.notFound("User", userId));

        ResourceException error = assertThrows(ResourceException.class, () -> userService.findById(userId, authenticatedUser));

        assertEquals("User with id 99 not found.", error.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully from db")
    void shouldDeleteUserSuccessfully() {

        Long userId = 1L;
        when(authenticatedUser.getUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId, authenticatedUser);

        verify(userRepository, times(1)).deleteById(userId);
    }
}