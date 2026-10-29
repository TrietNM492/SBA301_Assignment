package com.edu.fpt.dto.response;

import com.edu.fpt.domain.entity.Category;

import java.time.Instant;

public record NewsResponse(
        Long newsArticleID,
        String newsTitle,
        String headline,
        Instant createdDate,
        String newsContent,
        String newsSource,
        Long categoryID,
        Boolean newsStatus,
        Long updatedByID,
        Instant modifiedDate

) {
}
