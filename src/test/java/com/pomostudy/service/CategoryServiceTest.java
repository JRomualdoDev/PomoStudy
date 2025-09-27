package com.pomostudy.service;

import com.pomostudy.mapper.CategoryMapper;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private Category category;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@example.com");

        category = new Category();
        category.setId(1L);
        category.setName("testCategory");
        category.setColor("#fff");
        category.setIcon("test.icon");
        category.setUserCategory(user);

        categoryRequestDTO = new CategoryRequestDTO(
                "testCategory",
                "#fff",
                "test.icon",
                user.getId()
        );

        categoryResponseDTO = new CategoryResponseDTO(
                1L,
                "testCategory",
                "#fff",
                "test.icon"
        );
    }

    @Test
    @DisplayName("Should save Category with success in the db")
    void shouldSaveCategorySuccessfully() {

        when(categoryMapper.toCreateCategory(any(CategoryRequestDTO.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryResponseDTO(any(Category.class))).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.save(categoryRequestDTO);

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("testCategory", result.name());
        assertEquals("#fff", result.color());
        assertEquals("test.icon", result.icon());

        verify(categoryMapper, times(1)).toCreateCategory(any(CategoryRequestDTO.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toCategoryResponseDTO(any(Category.class));
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowExceptionWhenUserNotFound() {

        when(categoryMapper.toCreateCategory(any(CategoryRequestDTO.class))).thenThrow(ResourceExceptionFactory.notFound("User", categoryRequestDTO.userId()));

        ResourceException error = assertThrows(ResourceException.class, () -> categoryService.save(categoryRequestDTO));
        assertEquals("User with id 1 not found.", error.getMessage());

        verify(categoryMapper, times(1)).toCreateCategory(categoryRequestDTO);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should edit category successfully in the db")
    void shouldEditCategorySuccessfully() {
        Long categoryId = 1L;
        when(categoryMapper.toUpdateCategory(categoryRequestDTO, categoryId)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryResponseDTO(any(Category.class))).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.edit(categoryRequestDTO, categoryId);

        assertNotNull(result);
        assertEquals("testCategory", result.name());
        assertEquals("#fff", result.color());
        assertEquals("test.icon", result.icon());

        verify(categoryMapper, times(1)).toUpdateCategory(categoryRequestDTO, categoryId);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toCategoryResponseDTO(category);
    }

    @Test
    @DisplayName("Should throw exception when user is not belong to category")
    void shouldThrowExceptionWhenUserNotBelongToCategory() {

        Long categoryId = 1L;
        when(categoryMapper.toUpdateCategory(categoryRequestDTO, categoryId)).thenThrow(ResourceExceptionFactory.notFound("Category", categoryRequestDTO.userId()));

        ResourceException error = assertThrows(ResourceException.class, () -> categoryService.edit(categoryRequestDTO, categoryId));

        assertEquals("Category with id 1 not found.", error.getMessage());

        verify(categoryMapper, times(1)).toUpdateCategory(categoryRequestDTO, categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should throw exception when category does not found")
    void shouldThrowExceptionWhenCategoryNotFound() {

        Long categoryId = 1L;
        when(categoryMapper.toUpdateCategory(categoryRequestDTO, categoryId)).thenThrow(ResourceExceptionFactory.notFound("Category", categoryId));

        ResourceException error = assertThrows(ResourceException.class, () -> categoryService.edit(categoryRequestDTO, categoryId));

        assertEquals("Category with id 1 not found.", error.getMessage());

        verify(categoryMapper, times(1)).toUpdateCategory(categoryRequestDTO, categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Should be pass correct pagination parameters to service and return 200 OK")
    void shouldFindAllCategoriesDBWithStatus200() {

        List<Category> categoryResponse = Collections.singletonList(category);
        Pageable pageable = PageRequest.of(0,10);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(categoryResponse));
        when(categoryMapper.toCategoryResponseDTO(category)).thenReturn(categoryResponseDTO);

        Page<CategoryResponseDTO> result = categoryService.findAll(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        assertEquals(categoryResponseDTO,result.getContent().getFirst());
        assertEquals(categoryRequestDTO.name(), result.getContent().getFirst().name());

        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should find the category from id when the category exist in the db")
    void shouldFindCategoryFromIdWhenCategoryExistDB() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Optional<CategoryResponseDTO> result = Optional.ofNullable(categoryService.findById(categoryId));

        assertTrue(result.isPresent());
        assertEquals(categoryResponseDTO, result.get());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Should get Exception when do not find category from id")
    void shouldGetExceptionWhenNotFindCategoryFromId() {
        Long categoryId = 99L;
        when(categoryRepository.findById(categoryId)).thenThrow(ResourceExceptionFactory.notFound("Category", categoryId));

        ResourceException error = assertThrows(ResourceException.class, () -> categoryService.findById(categoryId));

        assertEquals("Category with id 99 not found.", error.getMessage());

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Should delete category succefully from db")
    void shouldDeleteTaskSuccessfullyDB() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.ofNullable(category));
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.delete(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}