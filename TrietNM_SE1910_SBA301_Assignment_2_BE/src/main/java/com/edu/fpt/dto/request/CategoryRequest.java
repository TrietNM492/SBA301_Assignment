package com.edu.fpt.dto.request;

import com.edu.fpt.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotBlank(message = "Category name can't be empty!")
        String categoryName,
        String categoryDesciption,
        @NotNull(message = "This can't be null")
        Category parentCategoryID

) {
}
