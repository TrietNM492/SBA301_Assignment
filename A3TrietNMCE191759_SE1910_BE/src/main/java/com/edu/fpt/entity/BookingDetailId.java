package com.edu.fpt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class BookingDetailId implements Serializable {
    private static final long serialVersionUID = -8615826760920270699L;
    @NotNull
    @Column(name = "BookingReservationID", nullable = false)
    private Integer bookingReservationID;

    @NotNull
    @Column(name = "RoomID", nullable = false)
    private Integer roomID;


}