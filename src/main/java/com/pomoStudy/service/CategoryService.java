package com.pomoStudy.service;

import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.dto.category.CategoryUpdateRequestDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepository;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;


    public void save(CategoryRequestDTO categoryRequestDTO) {

            Optional<User> user = userRepository.findById(categoryRequestDTO.userId());

            if (user.isEmpty())
                throw ResourceExceptionFactory.notFound("User", categoryRequestDTO.userId());

        try {
            Category category = new Category();
            category.setName(categoryRequestDTO.name());
            category.setColor(categoryRequestDTO.color());
            category.setIcon(categoryRequestDTO.icon());
            category.setUser_category(user.get());

            categoryRepository.save(category);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error create category.");
        }
    }

    public void edit(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long id) {
        Optional<User> user = userRepository.findById(categoryUpdateRequestDTO.userId());
        if (user.isEmpty()) throw ResourceExceptionFactory.notFound("User", categoryUpdateRequestDTO.userId());

        Category categoryUpdate = categoryRepository.findById(id)
                .filter((category ) -> category.getId().equals(categoryUpdateRequestDTO.userId()))
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));

        try {
            categoryUpdate.setName(categoryUpdateRequestDTO.name());
            categoryUpdate.setColor(categoryUpdateRequestDTO.color());
            categoryUpdate.setIcon(categoryUpdateRequestDTO.icon());

            categoryRepository.save(categoryUpdate);

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
