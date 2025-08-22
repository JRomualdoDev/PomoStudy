package com.pomoStudy.controller;

import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.dto.category.CategoryResponseDTO;
import com.pomoStudy.dto.category.CategoryUpdateRequestDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {

        categoryService.save(categoryRequestDTO);

        return ResponseEntity.status(201).body("Category created with success.");
    }

    @PutMapping("{id}")
    public ResponseEntity<String> editCategory(@RequestBody CategoryUpdateRequestDTO categoryUpdateRequestDTO, @PathVariable("id") Long id) {

        categoryService.edit(categoryUpdateRequestDTO, id);
        return ResponseEntity.ok("Category edited with success");
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findALL() {
        List<Category> categories = categoryService.findAll();
        List<CategoryResponseDTO> responseDTO = categories.stream()
                .map(CategoryResponseDTO::new)
                .collect(Collectors.toList());
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
