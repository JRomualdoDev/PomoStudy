package com.pomostudy.service;

import com.pomostudy.dto.category.CategoryMapper;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.exception.ResourceException;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final  CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO save(CategoryRequestDTO categoryRequestDTO) {

            Category categorySave = categoryRepository.save(categoryMapper.toCreateCategory(categoryRequestDTO));

            return categoryMapper.toCategoryResponseDTO(categorySave);
    }

    public CategoryResponseDTO edit(CategoryRequestDTO categoryRequestDTO, Long id) {

            Category categoryUpdate = categoryRepository.save(categoryMapper.toUpdateCategory(categoryRequestDTO, id));

            return categoryMapper.toCategoryResponseDTO(categoryUpdate);
    }

    public Page<CategoryResponseDTO> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(categoryMapper::toCategoryResponseDTO);
    }

    public CategoryResponseDTO findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));
    }

    public void delete(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));
        categoryRepository.deleteById(id);
    }

}
