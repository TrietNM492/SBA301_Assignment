package com.edu.fpt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username can't be empty!")
        String username,
        @NotBlank(message = "Password can't be empty!")
        String password
) {
}
