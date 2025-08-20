package com.pomoStudy.controller;

import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.dto.category.CategoryUpdateRequestDTO;
import com.pomoStudy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/category")
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
}
