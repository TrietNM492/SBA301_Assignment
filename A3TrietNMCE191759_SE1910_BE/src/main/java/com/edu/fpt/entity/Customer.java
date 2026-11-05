package com.edu.fpt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "CustomerFullName", nullable = false, length = 50)
    private String customerFullName;

    @Size(max = 12)
    @NotNull
    @Nationalized
    @Column(name = "Telephone", nullable = false, length = 12)
    private String telephone;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "EmailAddress", nullable = false, length = 100)
    private String emailAddress;

    @Column(name = "CustomerBirthday")
    private LocalDate customerBirthday;

    @Column(name = "CustomerStatus", columnDefinition = "tinyint")
    private Short customerStatus;  // 1 active, 0 deactivate

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Password", nullable = false)
    private String password;


}