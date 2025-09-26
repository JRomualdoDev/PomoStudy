package com.pomostudy.controller;

import com.pomostudy.config.security.SecurityConfigurations;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/category")
@Tag(name = "category", description = "Controller for saving, edit, search and delete category.")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create data category", description = "Method for create data category")
    @ApiResponse(responseCode = "201", description = "Category created with success")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, UriComponentsBuilder ucb) {

        CategoryResponseDTO categoryResponseDTO = categoryService.save(categoryRequestDTO);

        URI locationOfNewCategory = ucb
                .path("api/category/{id}")
                .buildAndExpand(categoryResponseDTO.id())
                .toUri();

        return ResponseEntity.created(locationOfNewCategory).body(categoryResponseDTO);
    }

    @PutMapping("{id}")
    @Operation(summary = "Edit data category", description = "Method for edit data category")
    @ApiResponse(responseCode = "200", description = "Category edited with success")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CategoryResponseDTO> editCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @PathVariable("id") Long id) {

        CategoryResponseDTO categoryResponseDTO = categoryService.edit(categoryRequestDTO, id);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping
    @Operation(summary = "List all data category", description = "Method for list data category")
    @ApiResponse(responseCode = "200", description = "Category listed successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Page<CategoryResponseDTO>> findALL(
            @Parameter(
                    name = "pageable",
                    in = ParameterIn.QUERY,
                    description = "Pagination and sorting object. **To use default values, send an empty object: `{}`**.",
                    examples = {
                            @ExampleObject(
                                    name = "Default Pagination",
                                    summary = "Fetch with default values",
                                    value = "{}"
                            ),
                            @ExampleObject(
                                    name = "Custom Pagination",
                                    summary = "Fetching the first page with 10 items",
                                    value = "{\"page\": 0, \"size\": 10, \"sort\": \"name,asc\"}"
                            )
                    },
                    schema = @Schema(type = "object")
            )
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable
            ) {

        Page<CategoryResponseDTO> listCategories = categoryService.findAll(pageable);

        return ResponseEntity.ok(listCategories);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find data category for id", description = "Method for search data category for the id")
    @ApiResponse(responseCode = "200", description = "Category listed successfully")
    @ApiResponse(responseCode = "404", description = "Category id not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CategoryResponseDTO> findCategoryById(@PathVariable("id") Long id) {

        return ResponseEntity.ok(categoryService.findById(id));

    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete data category for id", description = "Method for deleting data category for the id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
