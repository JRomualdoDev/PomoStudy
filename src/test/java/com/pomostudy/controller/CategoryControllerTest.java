package com.pomostudy.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.security.WithMockAuthenticatedUser;
import com.pomostudy.service.CategoryService;
import com.pomostudy.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfigurations.class)
@ActiveProfiles("test")
//@WithMockUser(roles = "USER")
@WithMockAuthenticatedUser
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private Category category;
    private final Long categoryID = 1L;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        category = new Category();
        category.setId(1L);
        category.setName("testCategory");
        category.setColor("#fff");
        category.setIcon("test.icon");
        category.setUser(user);

        categoryRequestDTO = new CategoryRequestDTO(
                "testCategory",
                "#fff",
                "test.icon"
        );

        categoryResponseDTO = new CategoryResponseDTO(
                1L,
                "testCategory",
                "#fff",
                "test.icon"
        );

    }

    @Test
    @DisplayName("Should be create category and return status 201 created")
    void shouldCreateCategoryReturnStatus201() throws Exception {

        when(categoryService.save(any(CategoryRequestDTO.class), any(AuthenticatedUser.class))).thenReturn(categoryResponseDTO);

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("api/category/1")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("testCategory")));
    }

    @Test
    @DisplayName("Should be return 400 Bad request when create a new category with invalid data")
    void shouldBeReturn400BadRequestWhenCreateNewCategoryWithInvalidData() throws Exception {

        CategoryRequestDTO invalidCategoryRequestDTO = new CategoryRequestDTO(
                null,
                "loremipsumloremipsumloremipsum",
                "test.icon"
                );

        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be edit category and return status 200 ok")
    void shouldEditCategoryAndReturnStatus200OK() throws Exception {

        when(categoryService.edit(any(CategoryRequestDTO.class), any(AuthenticatedUser.class), anyLong())).thenReturn(categoryResponseDTO);

        mockMvc.perform(put("/api/category/{id}", categoryID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("testCategory"));
    }

    @Test
    @DisplayName("Should be return 404 Not Found for try edit non-existent category")
    void shouldReturn404TryEditNonExistentCategory() throws Exception {

        when(categoryService.edit(any(CategoryRequestDTO.class), any(AuthenticatedUser.class), anyLong()))
                .thenThrow(ResourceExceptionFactory.notFound("Category", categoryID));

        mockMvc.perform(put("/api/category/{id}", categoryID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should be return all Category with status 200 OK")
    void shouldReturnAllCategoryWithStatus200() throws Exception {

        Pageable pageable = PageRequest.of(0,10);
        Page<CategoryResponseDTO> categoryPage = new PageImpl<>(List.of(categoryResponseDTO), pageable, 1);

        when(categoryService.findAll(any(Pageable.class), any(AuthenticatedUser.class))).thenReturn(categoryPage);

        mockMvc.perform(get("/api/category")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("testCategory")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

    }

    @Test
    @DisplayName("Should be found one category for the id and return 200 OK")
    void shouldFoundCategoryForTheIdReturn200() throws Exception {

        when(categoryService.findById(anyLong(), any(AuthenticatedUser.class))).thenReturn(categoryResponseDTO);

        mockMvc.perform(get("/api/category/{id}", categoryID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testCategory"))
                .andExpect(jsonPath("$.color").value("#fff"))
                .andExpect(jsonPath("$.icon").value("test.icon"));
    }

    @Test
    @DisplayName("Should be delete with success and return status 204 No Content")
    void shouldBeDeleteWithSuccessReturnStatus204() throws Exception {

        when(categoryService.findById(anyLong(), any(AuthenticatedUser.class))).thenReturn(categoryResponseDTO);
        doNothing().when(categoryService).delete(anyLong(), any(AuthenticatedUser.class));

        mockMvc.perform(delete("/api/category/{id}", categoryID))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(anyLong(), any(AuthenticatedUser.class));
    }
}