package com.pomostudy.service;

import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import com.pomostudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    private User user;

    @BeforeEach
    void setUp() {
       user = new User(
                "Test",
                "test@test.com",
                "Abc@abc",
                UserRole.USER
        );
    }

    @Test
    @DisplayName("Should return UserDetails when user is found for email")
    void shouldReturnUserDetailsWhenUserIsFoundForEmail() {
        String email = "test@test.com";

        when(userRepository.findUserByEmail(email)).thenReturn(this.user);

        UserDetails userDetails = authorizationService.loadUserByUsername(email);

        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(email, userDetails.getUsername(), "The username should be the same as email");

        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    @DisplayName("Should be throw UsernameNotFoundException when user is not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserIsNotFound() {
        String notExistingEmail = "abc@abc.com";

        when(userRepository.findUserByEmail(notExistingEmail)).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> authorizationService.loadUserByUsername(notExistingEmail));

        verify(userRepository, times(1)).findUserByEmail(notExistingEmail);
    }
}