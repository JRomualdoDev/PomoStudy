package com.pomostudy.controller;

import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {

        CategoryResponseDTO categoryResponseDTO = categoryService.save(categoryRequestDTO);

        return ResponseEntity.status(201).body(categoryResponseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> editCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable("id") Long id) {

        CategoryResponseDTO categoryResponseDTO = categoryService.edit(categoryRequestDTO, id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findALL() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponseDTO> responseDTO = categories.stream()
                .map(CategoryResponseDTO::new)
                .toList();
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("id") Long id) {
        CategoryResponseDTO categoryResponseDTO = categoryService.findById(id)
                .map(CategoryResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));

        return ResponseEntity.ok(categoryResponseDTO);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        categoryService.findById(id)
                        .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
