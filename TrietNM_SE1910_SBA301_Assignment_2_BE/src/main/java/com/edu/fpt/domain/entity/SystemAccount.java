package com.edu.fpt.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "SystemAccount")
@NoArgsConstructor
@AllArgsConstructor
public class SystemAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID", nullable = false)
    private Long id;

    @Size(max = 100)
    @Nationalized
    @Column(name = "AccountName", length = 100)
    private String accountName;

    @Size(max = 70)
    @Nationalized
    @Column(name = "AccountEmail", length = 70)
    private String accountEmail;

    @Column(name = "AccountRole")
    private Integer accountRole;

    @Size(max = 70)
    @Nationalized
    @Column(name = "AccountPassword", length = 70)
    private String accountPassword;


}