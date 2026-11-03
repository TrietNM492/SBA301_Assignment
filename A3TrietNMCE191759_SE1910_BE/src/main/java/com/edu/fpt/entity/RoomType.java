package com.edu.fpt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "RoomType")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomTypeID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "RoomTypeName", nullable = false, length = 50)
    private String roomTypeName;

    @Size(max = 250)
    @Nationalized
    @Column(name = "TypeDescription", length = 250)
    private String typeDescription;

    @Size(max = 250)
    @Nationalized
    @Column(name = "TypeNote", length = 250)
    private String typeNote;


}