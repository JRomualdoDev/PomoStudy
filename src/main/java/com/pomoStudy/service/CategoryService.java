package com.pomoStudy.service;

import com.pomoStudy.dto.category.CategoryMapper;
import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.dto.category.CategoryResponseDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.exception.ResourceException;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    final private CategoryRepository categoryRepository;
    final private UserRepository userRepository;
    final private CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository
            , CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponseDTO save(CategoryRequestDTO categoryRequestDTO) {

        try {
            Category categorySave = categoryRepository.save(categoryMapper.toCategory(categoryRequestDTO, null));

            return categoryMapper.toResponseDTO(categorySave);

        } catch(ResourceException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error create category.");
        }
    }

    public CategoryResponseDTO edit(CategoryRequestDTO categoryRequestDTO, Long id) {

        try {
            Category categoryUpdade = categoryRepository.save(categoryMapper.toCategory(categoryRequestDTO, id));

            return categoryMapper.toResponseDTO(categoryUpdade);
        } catch (ResourceException e) {
            System.out.println(e.getMessage());
            throw e;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updated category.");
        }
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

}
