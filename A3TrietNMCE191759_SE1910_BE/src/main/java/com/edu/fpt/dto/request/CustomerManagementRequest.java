package com.edu.fpt.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerManagementRequest {
    @NotBlank
    @Size(max = 50)
    private String customerFullName;
    @NotBlank
    @Size(max = 12)
    private String telephone;
    @NotBlank
    @Email
    @Size(max = 100)
    private String emailAddress;
    private LocalDate customerBirthday;
    private Short customerStatus;
    @Size(min = 8, max = 72)
    private String password;
}
