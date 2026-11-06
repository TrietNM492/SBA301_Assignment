package com.edu.fpt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Customer Name cant be null or blank!")
    @Size(max = 50)
    private String customerFullName;
    @Size(max = 12)
    private String telephone;
    private LocalDate customerBirthday;
}
