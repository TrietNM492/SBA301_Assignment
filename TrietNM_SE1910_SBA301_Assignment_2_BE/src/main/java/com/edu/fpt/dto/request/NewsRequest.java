package com.edu.fpt.dto.request;

import com.edu.fpt.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewsRequest(
        @NotBlank(message = "News title can't be blank.")
        @Size(max = 400, message = "News title must not exceed 400 characters.")
        String newsTitle,

        @NotBlank(message = "Headline can't be blank.")
        @Size(max = 150, message = "Headline must not exceed 150 characters.")
        String headline,

        @NotBlank(message = "News content can't be blank.")
        @Size(max = 4000, message = "News content must not exceed 4000 characters.")
        String newsContent,

        @Size(max = 400, message = "News source must not exceed 400 characters.")
        String newsSource,

        @NotNull(message = "A category must be selected.")
        Category categoryID

) {
}
