package com.pomostudy.dto.category;

import com.pomostudy.entity.Category;

public record CategoryResponseDTO(Long id,String name, String color, String icon) {

    public CategoryResponseDTO(Category category) {
        this(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getIcon()
        );
    }
}
