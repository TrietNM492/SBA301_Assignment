package com.edu.fpt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequest(
        @NotBlank(message = "Can't make your name empty!")
        String accountName
) {
}
