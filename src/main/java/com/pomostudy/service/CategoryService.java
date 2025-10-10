package com.pomostudy.service;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.entity.User;
import com.pomostudy.mapper.CategoryMapper;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final  CategoryMapper categoryMapper;

    private final AuthorizationService authorizationService;

    private static final String CATEGORY = "Category";

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            AuthorizationService authorizationService
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.authorizationService = authorizationService;
    }

    public CategoryResponseDTO save(CategoryRequestDTO categoryRequestDTO, AuthenticatedUser authenticatedUser) {

            Category categorySave = categoryRepository.save(categoryMapper.toCreateCategory(categoryRequestDTO, authenticatedUser));
            return categoryMapper.toCategoryResponseDTO(categorySave);
    }

    public CategoryResponseDTO edit(CategoryRequestDTO categoryRequestDTO, AuthenticatedUser authenticatedUser, Long id) {

            Long userId = authenticatedUser.getUser().getId();

            if (!authorizationService.isOwner(Category.class, id, userId) && !authenticatedUser.isAdmin()) {
                throw ResourceExceptionFactory.notFound(CATEGORY, id);
            }

            Category categoryUpdate = categoryRepository.save(categoryMapper.toUpdateCategory(categoryRequestDTO, authenticatedUser.getUser(), id));
            return categoryMapper.toCategoryResponseDTO(categoryUpdate);
    }

    public Page<CategoryResponseDTO> findAll(Pageable pageable, AuthenticatedUser authenticatedUser) {
        Page<Category> categoryPage;

        if (authenticatedUser.isAdmin()) {
            categoryPage = categoryRepository.findAll(pageable);
        } else {
            categoryPage = categoryRepository.findByUser(authenticatedUser.getUser(), pageable);
        }

        return categoryPage.map(categoryMapper::toCategoryResponseDTO);
    }

    public CategoryResponseDTO findById(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        // Not user Owner or not Admin
        if (!authorizationService.isOwner(Category.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(CATEGORY, id);
        }

        return categoryRepository.findById(id)
                .map(CategoryResponseDTO::new)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(CATEGORY, id));
    }

    public void delete(Long id, AuthenticatedUser authenticatedUser) {

        Long userId = authenticatedUser.getUser().getId();

        if (!authorizationService.isOwner(Category.class, id, userId) && !authenticatedUser.isAdmin()) {
            throw ResourceExceptionFactory.notFound(CATEGORY, id);
        }

        categoryRepository.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound(CATEGORY, id));
        categoryRepository.deleteById(id);
    }

}
