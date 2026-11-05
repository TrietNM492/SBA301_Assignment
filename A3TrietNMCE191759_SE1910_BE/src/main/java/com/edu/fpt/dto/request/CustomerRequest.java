package com.edu.fpt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Customer Name cant be null or blank!")
    private String customerFullName;
    private String telephone;
    private LocalDate customerBirthday;
}
