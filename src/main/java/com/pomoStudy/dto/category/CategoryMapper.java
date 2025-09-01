package com.pomoStudy.dto.category;

import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.Task;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.UserRepository;
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
               category.getName(),
               category.getColor(),
               category.getIcon()
        );
    }

    public Category toCategory(CategoryRequestDTO categoryRequestDTO, Long id) {

        Category categoryUpdateOrCreate;

        if (id != null) {
            categoryUpdateOrCreate = categoryRepository.findById(id)
                    .filter((category ) -> category.getUser_category().getId().equals(categoryRequestDTO.userId()))
                    .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));
        } else {
            categoryUpdateOrCreate = new Category();
        }

        Optional<User> user = userRepository.findById(categoryRequestDTO.userId());
        if (user.isEmpty())
            throw ResourceExceptionFactory.notFound("User", categoryRequestDTO.userId());

        categoryUpdateOrCreate.setName(categoryRequestDTO.name());
        categoryUpdateOrCreate.setColor(categoryRequestDTO.color());
        categoryUpdateOrCreate.setIcon(categoryRequestDTO.icon());
        categoryUpdateOrCreate.setUser_category(user.get());

        return categoryUpdateOrCreate;

    }
}
