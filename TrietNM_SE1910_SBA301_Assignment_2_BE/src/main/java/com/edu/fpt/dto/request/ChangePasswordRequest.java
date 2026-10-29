package com.edu.fpt.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Password can't be blank!")
        String oldPass,

        @NotBlank(message = "Password can't be blank!")
        String newPass
) {
}
