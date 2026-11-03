package com.edu.fpt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "BookingReservation")
public class BookingReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingReservationID", nullable = false)
    private Integer id;

    @Column(name = "BookingDate")
    private LocalDate bookingDate;

    @Column(name = "TotalPrice", precision = 18, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customerID;

    @Column(name = "BookingStatus", columnDefinition = "tinyint")
    private Short bookingStatus;


}