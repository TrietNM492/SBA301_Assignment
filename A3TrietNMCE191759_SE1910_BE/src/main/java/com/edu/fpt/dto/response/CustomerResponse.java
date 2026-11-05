package com.edu.fpt.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Integer id;
    private String customerFullName;
    private String telephone;
    private String emailAddress;
    private LocalDate customerBirthday;
    private Short customerStatus;
}
