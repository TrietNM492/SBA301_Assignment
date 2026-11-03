package com.edu.fpt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "RoomInformation")
public class RoomInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "RoomNumber", nullable = false, length = 50)
    private String roomNumber;

    @Size(max = 220)
    @Nationalized
    @Column(name = "RoomDetailDescription", length = 220)
    private String roomDetailDescription;

    @Column(name = "RoomMaxCapacity")
    private Integer roomMaxCapacity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RoomTypeID", nullable = false)
    private RoomType roomTypeID;

    @Column(name = "RoomStatus", columnDefinition = "tinyint")
    private Short roomStatus;

    @Column(name = "RoomPricePerDay", precision = 18, scale = 2)
    private BigDecimal roomPricePerDay;


}