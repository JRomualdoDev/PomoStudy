package com.pomostudy.dto.category;

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

    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon()
        );
    }

    public Category toCreateCategory(CategoryRequestDTO categoryRequestDTO) {

        Category category = new Category();

        Optional<User> user = userRepository.findById(categoryRequestDTO.userId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", categoryRequestDTO.userId());

        category.setName(categoryRequestDTO.name());
        category.setColor(categoryRequestDTO.color());
        category.setIcon(categoryRequestDTO.icon());
        category.setUserCategory(user.get());

        return category;
    }

    public Category toUpdateCategory(CategoryRequestDTO categoryRequestDTO, Long id) {

        Category category = categoryRepository.findById(id)
                    .filter(c-> c.getUserCategory().getId().equals(categoryRequestDTO.userId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));

        Optional<User> user = userRepository.findById(categoryRequestDTO.userId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", categoryRequestDTO.userId());

        category.setName(categoryRequestDTO.name());
        category.setColor(categoryRequestDTO.color());
        category.setIcon(categoryRequestDTO.icon());
        category.setUserCategory(user.get());

        return category;
    }
}
