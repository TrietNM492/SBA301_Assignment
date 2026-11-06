package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Email(message = "Email must match the validation!")
    @NotBlank(message = "Email cant be null or empty!")
    private String email;
    @NotBlank(message = "Password cant be null or blank!")
    @Size(min = 8, max = 72, message = "Password must contain 8 to 72 characters")
    private String password;
    @NotBlank(message = "Password cant be null or blank!")
    private String rePassword;
    @NotBlank(message = "Customer name is required")
    @Size(max = 50)
    private String customerFullName;
    @NotBlank(message = "Telephone is required")
    @Size(max = 12)
    private String telephone;
    private LocalDate customerBirthday;
}
