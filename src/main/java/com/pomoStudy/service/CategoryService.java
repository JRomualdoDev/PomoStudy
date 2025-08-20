package com.pomoStudy.service;

import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.dto.category.CategoryUpdateRequestDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.entity.User;
import com.pomoStudy.exception.ResourceExceptionFactory;
import com.pomoStudy.repository.CategoryRepositoty;
import com.pomoStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepositoty categoryRepositoty;
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

            categoryRepositoty.save(category);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error create category.");
        }
    }

    public void edit(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw ResourceExceptionFactory.notFound("User", id);

        Category categoryUpdate = categoryRepositoty.findById(id)
                .orElseThrow(() -> ResourceExceptionFactory.notFound("Category", id));

        try {
            categoryUpdate.setName(categoryUpdateRequestDTO.name());
            categoryUpdate.setColor(categoryUpdateRequestDTO.color());
            categoryUpdate.setIcon(categoryUpdateRequestDTO.icon());
            categoryUpdate.setUser_category(user.get());

            categoryRepositoty.save(categoryUpdate);

        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error updated category.");
        }
    }

}
