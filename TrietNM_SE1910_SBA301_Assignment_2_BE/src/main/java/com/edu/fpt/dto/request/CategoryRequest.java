package com.edu.fpt.dto.request;

import com.edu.fpt.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Category name can't be empty!")
        String categoryName,
        String categoryDesciption,
        Category parentCategoryID

) {
}
