package com.pomoStudy.service;

import com.pomoStudy.dto.category.CategoryRequestDTO;
import com.pomoStudy.entity.Category;
import com.pomoStudy.repository.CategoryRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    CategoryRepositoty categoryRepositoty;

    public void save(CategoryRequestDTO categoryRequestDTO) {
        try {
            Category category = new Category();
            category.setName(categoryRequestDTO.name());
            category.setColor(categoryRequestDTO.color());
            category.setIcon(categoryRequestDTO.icon());
            category.setUser_category(categoryRequestDTO.userId());

            categoryRepositoty.save(category);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error create category.");
        }
    }

}
