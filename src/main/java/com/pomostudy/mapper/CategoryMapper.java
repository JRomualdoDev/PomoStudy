package com.pomostudy.mapper;

import com.pomostudy.config.security.AuthenticatedUser;
import com.pomostudy.dto.category.CategoryRequestDTO;
import com.pomostudy.dto.category.CategoryResponseDTO;
import com.pomostudy.entity.Category;
import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.CategoryRepository;
import com.pomostudy.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMapper {

    UserRepository userRepository;
    CategoryRepository categoryRepository;

    public CategoryMapper(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO toCategoryResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon()
        );
    }

    public Category toCreateCategory(CategoryRequestDTO categoryRequestDTO, AuthenticatedUser authenticatedUser) {

        Category category = new Category();

        Optional<User> user = userRepository.findById(authenticatedUser.getUser().getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getUser().getId());

        category.setName(categoryRequestDTO.name());
        category.setColor(categoryRequestDTO.color());
        category.setIcon(categoryRequestDTO.icon());
        category.setUser(authenticatedUser.getUser());

        return category;
    }

    public Category toUpdateCategory(CategoryRequestDTO categoryRequestDTO, User authenticatedUser, Long id) {

        Category category = categoryRepository.findById(id)
                    .filter(c-> c.getUser().getId().equals(authenticatedUser.getId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));

        Optional<User> user = userRepository.findById(authenticatedUser.getId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", authenticatedUser.getId());

        category.setName(categoryRequestDTO.name());
        category.setColor(categoryRequestDTO.color());
        category.setIcon(categoryRequestDTO.icon());
        category.setUser(user.get());

        return category;
    }
}
