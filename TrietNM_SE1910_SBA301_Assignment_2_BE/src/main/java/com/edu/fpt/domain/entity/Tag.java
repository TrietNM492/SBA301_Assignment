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
@Table(name = "Tag")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TagID", nullable = false)
    private Long id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "TagName", length = 50)
    private String tagName;

    @Size(max = 400)
    @Nationalized
    @Column(name = "Note", length = 400)
    private String note;


}