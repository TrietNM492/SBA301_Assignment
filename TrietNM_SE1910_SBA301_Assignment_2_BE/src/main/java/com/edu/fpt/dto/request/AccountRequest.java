package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
        @NotBlank(message = "Name cant be blank!")
        String accountName,
        @Email(message = "Email must ")
        String accountEmail,
        @NotNull(message = "Must choose 1 role")
        Integer accountRole

) {
}
