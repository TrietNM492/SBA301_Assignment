package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRegister(
        @NotBlank(message = "Name cant be blank!")
        String accountName,
        @Email(message = "Email must ")
        String accountEmail,
        @NotBlank(message = "Password cant be blank")
        String password,
        @NotNull(message = "Password cant be blank")
        String password2nd
) {
}
