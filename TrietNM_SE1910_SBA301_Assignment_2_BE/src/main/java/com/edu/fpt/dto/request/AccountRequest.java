package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AccountRequest(
        @NotBlank(message = "Name cant be blank!")
        String accountName,
        @Email(message = "Email must ")
        String accountEmail,
        @NotBlank(message = "Must choose 1 role")
        Integer accountRole

) {
}
