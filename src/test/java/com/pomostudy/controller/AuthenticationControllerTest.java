package com.pomostudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.auth.AuthenticationDTO;
import com.pomostudy.dto.user.UserCreateRequestDTO;
import com.pomostudy.dto.user.UserResponseDTO;
import com.pomostudy.entity.User;
import com.pomostudy.enums.UserRole;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.service.AuthorizationService;
import com.pomostudy.service.TokenService;
import com.pomostudy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AuthenticationController.class)
@Import({SecurityConfigurations.class})
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;


    @MockBean
    private AuditingHandler auditingHandler;

    @MockBean
    private DateTimeProvider auditingDateTimeProvider;

    private User user;
    private AuthenticationDTO authenticationDTO;

    private UserCreateRequestDTO userCreateRequestDTO;
    private UserResponseDTO userResponseDTO;


    @BeforeEach
    void setUp() {
        authenticationDTO = new AuthenticationDTO("test@test.com", "Password@123" );

        userCreateRequestDTO = new UserCreateRequestDTO("testuser", "test@example.com", "Password@123");
        userResponseDTO = new UserResponseDTO(1L, "testuser", "test@example.com");

        user = new User();
        Long userId = 1L;
        user.setId(userId);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setRole(UserRole.ADMIN);

    }

    @Test
    @DisplayName("Should do login with success and return status 200 with JWT token")
    void shouldLoginSuccessReturnStatus200WithJWTToken() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        String expectedToken = "sample.jwt.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken(any(User.class))).thenReturn(expectedToken);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is(expectedToken)));
    }

    @Test
    @DisplayName("Should return 401 Unauthorized when credentials are invalid")
    void loginWithInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 201 Created on successful user registration")
    void registerWithValidDataShouldCreateUserAndReturnResponse() throws Exception {

        when(userService.save(any(UserCreateRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/user/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("testuser")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when registration data is invalid")
    void registerWithInvalidDataShouldReturnBadRequest() throws Exception {
        UserCreateRequestDTO invalidRequestDTO = new UserCreateRequestDTO("", "not-an-email", "123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error when email is already in use")
    void registerWithDuplicateEmailShouldReturnInternalServerError() throws Exception {

        when(userService.save(any(UserCreateRequestDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Email already exists."));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}