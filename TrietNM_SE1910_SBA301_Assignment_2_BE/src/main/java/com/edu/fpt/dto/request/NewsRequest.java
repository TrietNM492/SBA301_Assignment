package com.edu.fpt.dto.request;

import com.edu.fpt.domain.entity.Category;

public record NewsRequest(
        String newsTitle,
        String headline,
        String newsContent,
        String newsSource,
        Category categoryID,
        Long updatedByID

) {
}
