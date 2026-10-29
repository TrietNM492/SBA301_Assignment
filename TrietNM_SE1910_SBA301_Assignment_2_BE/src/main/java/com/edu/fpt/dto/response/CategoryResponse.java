package com.edu.fpt.dto.response;

import com.edu.fpt.domain.entity.Category;

public record CategoryResponse(
        Long id,
        String categoryName,
        String categoryDesciption,
        Long parentCategoryID,
        boolean isActive

) {
}
