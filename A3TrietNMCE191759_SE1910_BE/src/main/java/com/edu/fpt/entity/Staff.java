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
@Table(name = "Staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StaffID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "StaffFullName", nullable = false, length = 50)
    private String staffFullName;

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

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Role", columnDefinition = "tinyint not null")
    private Short role;

    @Column(name = "StaffStatus", columnDefinition = "tinyint")
    private Short staffStatus;

    @Column(name = "HireDate")
    private LocalDate hireDate;


}