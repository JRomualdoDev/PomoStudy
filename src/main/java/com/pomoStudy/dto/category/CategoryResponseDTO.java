package com.pomoStudy.dto.category;

import com.pomoStudy.entity.Category;

public record CategoryResponseDTO(String name, String color, String icon) {

    public CategoryResponseDTO(Category category) {
        this(category.getName(), category.getColor(), category.getIcon());
    }
}
