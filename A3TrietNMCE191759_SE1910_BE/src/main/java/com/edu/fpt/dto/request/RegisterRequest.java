package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    private String password;
    @NotBlank(message = "Password cant be null or blank!")
    private String rePassword;
}
